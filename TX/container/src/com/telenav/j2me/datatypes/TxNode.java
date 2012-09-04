/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.telenav.j2me.datatypes;

import java.io.UnsupportedEncodingException;
import java.util.Vector;



/**
 * @author alexg
 *
 * This is implementation of transmission data type
 */
public class TxNode 
{
    public static final int VERSION_50          = 50;
    public static final int VERSION_55          = 55;
    public static final int VERSION_551         = 551;

    public static final int HEADER_55           = 983276848;
    public static final int HEADER_551          = 983276849;

    
    public static final String loginfo = "com.telenav.j2me.datatypes.TxNode";   // #TV_STRIP
    
    private byte    masterByte = 0;
    private byte[]  statusBytes;
    private byte[]  buff;
    
    private  Vector children = null;
    private  Vector msgs = null;
    private byte [] binData = null;
    private  boolean isExpandChildren = true;
    
    private int size = 0;
    
    //private static int[] temp = new int[4]; //it's not thead safety and recursive safety
    
    private int version = VERSION_50;
    
    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }
    
    public int getNodeType()
    {
        int nodeType = -1;//undef
        if (getValuesCount() > 0) nodeType = (int) valueAt(0);
        return nodeType;
    }

    public int getAction()
    {
        int nodeType = -1;//undef
        if (getValuesCount() > 1) nodeType = (int) valueAt(1);
        return nodeType;
    }
    
    public int getStatus()
    {
        int nodeType = -1;//undef
        if (getValuesCount() > 2) nodeType = (int) valueAt(2);
        return nodeType;
    }

    /**
     * converts from byte array to TxNode
     * if isExpandChildren was set to false before serialization, then children will be kept
     * as binaries.
     * Max size of child binary is 65535 = (256 * 256 - 1), because we use 2 bytes to mark the 
     * binary child size.
     * @param buff
     * @param offset
     * @return
     */
    static public TxNode fromByteArray(byte[] buff, int offset)
    {
        return fromByteArray(buff, offset, null);
    }

    static public TxNode fromByteArray(byte[] buff, int offset, String encoding)
    {
        if (buff != null && buff.length >= offset + 4)
        {
            int headerCode = DataUtil.readInt(buff, offset);
            if (headerCode == HEADER_55)
            {
//              System.out.println("DataFactory::createNode: from 5.5 stream");
                TxNode node = TxNode.fromByteArray55(buff, offset + 4, encoding, VERSION_55);
                return node;
            }
            else if (headerCode == HEADER_551)
            {
//              System.out.println("DataFactory::createNode: from 5.5 stream");
                TxNode node = TxNode.fromByteArray55(buff, offset + 4, encoding, VERSION_551);
                return node;                
            }
        }
        
//      System.out.println("DataFactory::createNode: from 5.0 stream");
        return TxNode.fromByteArray50(buff, offset, encoding, VERSION_50);
    }
    
    /**
     * converts from byte array to TxNode
     * if isExpandChildren was set to false before serialization, then children will be kept
     * as binaries.
     * Max size of child binary is 65535 = (256 * 256 - 1), because we use 2 bytes to mark the 
     * binary child size.
     * @param buff
     * @param offset
     * @return
     */
    static private TxNode fromByteArray50(byte[] buff, int offset, String encoding, int ver)
    {
        if (buff == null || buff.length == 0) return new TxNode();
        
        int counter = offset;

        TxNode node = new TxNode();
        node.setVersion(ver);
        
        byte b0 = buff[counter++];
        
        node.masterByte = (byte) (b0 & (~0x7));
        
        int size = (0xFF & b0) >> 3;
                
        int index = size;
        if ((b0 & 0x1) > 0) size++;
        if ((b0 & 0x2) > 0) size++;
        if ((b0 & 0x4) > 0) size++;
        
        if (size > 0)
        {
            int num = size * 3;
            int statusSize = num / 8;
            if (num % 8 > 0) statusSize++;
        
            int b1startIndex = counter;
            counter += statusSize;
            int[] temp = new int[4];
            getPosIndices(size - 1, buff, b1startIndex, statusSize, temp);
            int buffSize = temp[0];
            
            int b2startIndex = counter;
            counter += buffSize;
            
            if ((b0 & 0x1) > 0) 
            {
                long nSize = valueAt(index, false, (byte)b0, buff, b1startIndex, statusSize, b2startIndex, buffSize);
                
                if (nSize >= Integer.MAX_VALUE)
                {
                    nSize -= Integer.MAX_VALUE;
                    node.isExpandChildren = false;
                }
                
                if (nSize > 0)
                {
                    if (node.isExpandChildren)
                    {
                        for (int i = 0; i < nSize; i++)
                        {
                            TxNode n = TxNode.fromByteArray50(buff, counter, encoding, ver);
                            node.addChild(n);
                            counter += n.size;
                        }
                    }
                    else
                    {
                        int childCounter = counter;
                        counter += nSize * 2;
                        for (int j = 0; j < nSize; j++)
                        {
                            int childSize = getIntFor(buff, childCounter);
                            childCounter += 2;

                            if (childSize < 0) childSize += 65536;
                            byte[] bb = new byte[childSize];
                            System.arraycopy(buff, counter, bb, 0, childSize);
                            counter += childSize;
                            node.addChild(bb);
                        }
                    }
                }

                index++;
            }
            
            if ((b0 & 0x2) > 0) 
            {
                int nSize = (int) valueAt(index, false, (byte)b0, buff, b1startIndex, statusSize, b2startIndex, buffSize);
                for (int i = 0; i < nSize; i++)
                {
                    int strLen = getIntFor(buff, counter);
                    counter += 2;
                    if (strLen < 0) strLen += 65536;
                    
//                  String str = new String(buff, counter, strLen);
//                  node.addMsg(str);
                    
                    //use given encoding to create string
                    try
                    {
                        String str = null;
                        if(encoding == null)
                        {
                            str = new String(buff, counter, strLen);
                        }
                        else
                        {
                            str = new String(buff, counter, strLen, encoding);
                        }
                        node.addMsg(str);
                    }
                    catch(UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    
                    counter += strLen;
                }
                index++;
            }
            
            if ((b0 & 0x4) > 0) 
            {
                int nSize = (int) valueAt(index, false, (byte)b0, buff, b1startIndex, statusSize, b2startIndex, buffSize);
                node.binData = new byte[nSize];
                for (int i = 0; i < nSize; i++)
                {
                    node.binData[i] = buff[counter++];  
                }
            }
            
            size = (0xFF & b0) >> 3;
            getPosIndices(size - 1, buff, b1startIndex, statusSize, temp);
            int origBuffSize    = temp[0];
            int origByteIndex   = temp[1];
            int origBitIndex    = temp[2];
            int origStatusSize  = origByteIndex;
            if (origBitIndex > 0) origStatusSize++;
            
            if (origStatusSize > 0)
            {
                if (origStatusSize == statusSize)
                {
                    node.statusBytes = new byte[statusSize];
                }
                else
                {
                    node.statusBytes = new byte[origStatusSize];
                }
                System.arraycopy(buff, b1startIndex, node.statusBytes, 0, node.statusBytes.length);
                if (origBitIndex > 0)
                {
                    int lastByteMask = 0;
                    for (int i = 0; i < origBitIndex; i++)
                    {
                        lastByteMask |= (0x1 << i);
                    }
                    node.statusBytes[origStatusSize - 1] &= lastByteMask;
                }
            }
            
            if (origBuffSize > 0)
            {
                if (origBuffSize == buffSize)
                {
                    node.buff = new byte[buffSize];
                }
                else
                {
                    node.buff = new byte[origBuffSize];
                }
                System.arraycopy(buff, b2startIndex, node.buff, 0, node.buff.length);
            }
        }
        
        node.size = counter - offset;
        return node;
    }
    
    /** converter to byte array */
    static public byte[] toByteArray(TxNode node)
    {
        return toByteArray(node, null);
    }

    static public byte[] toByteArray(TxNode node, String encoding)
    {
        if (node.getVersion() == VERSION_55)
        {
            return toByteArray55(node, encoding, HEADER_55, VERSION_55);
        }
        else if (node.getVersion() == VERSION_551)
        {
            return toByteArray55(node, encoding, HEADER_551, VERSION_551);
        }
        else
        {
            return toByteArray50(node, encoding, VERSION_50);           
        }
    }
    
    /**
     * converter to byte array by given encoding
     */
    static private byte[] toByteArray50(TxNode node, String encoding, int ver)
    {
        if (node == null) node = new TxNode();
        
        byte[] temp = null;
        byte[] buff = new byte[256];
        int counter = 0;
        
        int mask = 0;
        int addSize = 0;
        
        byte[][] b = new byte[3][];
        b[0] = new byte[] { node.masterByte };
        b[1] = node.statusBytes;
        b[2] = node.buff;
        if (node.children != null && node.children.size() > 0) 
        {
            mask = mask | 0x1;
            long childSize = node.children.size();
            
            if (!node.isExpandChildren)
            {
                childSize += Integer.MAX_VALUE; 
            }
            
            b = addValue(childSize, addSize++, b[0][0], b[1], b[2]);
        }
        
        if (node.msgs != null && node.msgs.size() > 0) 
        {
            mask = mask | 0x2;
            b = addValue(node.msgs.size(), addSize++, b[0][0], b[1], b[2]);
        }
         
        if (node.binData != null && node.binData.length > 0) 
        {
            mask = mask | 0x4;
            b = addValue(node.binData.length, addSize++, b[0][0], b[1], b[2]);
        } 
        
        b[0][0] = (byte) setBits(b[0][0], mask, 0);

        buff = DataUtil.setByte(buff, b[0][0], counter++);
        for (int i = 0; b[1] != null && i < b[1].length; i++)
        {
            buff = DataUtil.setByte(buff, b[1][i], counter++);
        }
        for (int i = 0; b[2] != null && i < b[2].length; i++)
        {
            buff = DataUtil.setByte(buff, b[2][i], counter++);
        }
        
        if (node.children != null)
        {
            int childCounter = counter;
            if (!node.isExpandChildren)
            {
                counter += node.children.size() * 2;
            }
        
            for (int j = 0; j < node.children.size(); j++)
            {
                Object obj = node.children.elementAt(j);
                if (obj == null) obj = new TxNode();
                if (obj instanceof TxNode)
                    temp = TxNode.toByteArray50((TxNode) obj, encoding, ver);
                else if (obj instanceof byte[])
                    temp = (byte[]) obj;
                else
                    temp = new byte[0];
                
                for (int i = 0; i < temp.length; i++)
                {
                    buff = DataUtil.setByte(buff, temp[i], counter++);
                }
                    
                if (!node.isExpandChildren)
                {
                    temp = getBytesFor(temp.length);
                    for (int i = 0; i < temp.length; i++) buff = DataUtil.setByte(buff, temp[i], childCounter++);
                }
            }
        }
        for (int j = 0; node.msgs != null && j < node.msgs.size(); j++)
        {
            String str = (String) node.msgs.elementAt(j);
            if (str != null)
            {
//              temp = str.getBytes();
                if(encoding == null)
                {
                    temp = str.getBytes();
                }
                else
                {
                    try
                    {
                        temp = str.getBytes(encoding);
                    }
                    catch(UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
                temp = new byte[0];
            
            int len = temp.length;
            if (len > 65535) len = 65535;
            
            byte[] lenBuff = getBytesFor(len);
            for (int i = 0; i < lenBuff.length; i++) buff = DataUtil.setByte(buff, lenBuff[i], counter++);
            for (int i = 0; i < len; i++) buff = DataUtil.setByte(buff, temp[i], counter++);
        }
        
        if (node.binData != null)
        {
            for (int i = 0;  i < node.binData.length; i++)
            {
                buff = DataUtil.setByte(buff, node.binData[i], counter++);
            }
        }
        
        byte[] retBuff = new byte[counter];
        for (int i = 0; i < counter; i++) retBuff[i] = buff[i];
        
        return retBuff;
    }
    
    public int getValuesCount()
    {
        return (0xFF & masterByte) >> 3;
    }
    
    public void addMsg(String str)
    {
        if (this.msgs == null) 
            this.msgs = new Vector();
        this.msgs.addElement(str);
    }
    
    public void insertMsgAt(String str, int index)
    {
        if (this.msgs == null) 
            this.msgs = new Vector();
        this.msgs.insertElementAt(str, index);
    }
    
    public void addChild(Object n)
    {
        if (this.children == null) 
            this.children = new Vector();
        this.children.addElement(n);
    }
    
    public void insertChildAt(Object n, int index)
    {
        if (this.children == null)
            this.children = new Vector();
        this.children.insertElementAt(n, index);
    }
    
    public void addValue(long v)
    {
        byte[][] b = addValue(v, -1, this.masterByte, this.statusBytes, this.buff);
        this.masterByte     = b[0][0];
        this.statusBytes    = b[1];
        this.buff           = b[2];
    }
    
    public int childrenSize()
    {
        if (this.children != null)
            return this.children.size();
        else
            return 0;
    }
    
    public int msgsSize()
    {
        if (this.msgs != null)
            return this.msgs.size();
        else
            return 0;
    }
    
    public TxNode childAt(int index)
    {
        if (this.children != null && index < this.children.size())
        {
            Object obj = this.children.elementAt(index);
            return (TxNode) obj;
        }
        
        return null;
    }
    
    public void setChildAt(TxNode child, int index)
    {
        this.children.setElementAt(child, index);
    }
    
    public void removeChildAt(int index)
    {
        this.children.removeElementAt(index);
    }
    
    public String msgAt(int index)
    {
        if (this.msgs != null && index < this.msgs.size())
        {
            return (String) this.msgs.elementAt(index);
        }
        
        return null;
    }
    
    public byte[] getBinData()
    {
        return this.binData;
    }
    
    public void setBinData(byte[] b)
    {
        this.binData = b;
    }
    
    public boolean isEmpty()
    {
        return getValuesCount() == 0 && childrenSize() == 0 && msgsSize() == 0 && binData == null;
    }

    public long valueAt(int index)
    {
        return valueAt(index, true, this.masterByte, this.statusBytes, this.buff);
    }
    
    //TODO: merge these two valueAt later
    static private long valueAt(int index, boolean isExternal, byte masterByte, byte[] statusBytes, byte[] buff)
    {
        // get number of current values added so far
        int size = (0xFF & masterByte) >> 3;
        if (isExternal && index >= size) return 0;
        
        int[] temp = new int[4];
        getPosIndices(index, statusBytes, 0, statusBytes.length, temp);
        int currPos     = temp[0];
        int valSize     = temp[3];
        
        return bytesToLong(buff, currPos - valSize, valSize);
    }
    
    static private long valueAt(int index, boolean isExternal, byte masterByte, byte[] buff, int b1startIndex, int b1length, int b2startIndex, int b2length)
    {
        // get number of current values added so far
        int size = (0xFF & masterByte) >> 3;
        if (isExternal && index >= size) return 0;
        
        int[] temp = new int[4];
        getPosIndices(index, buff, b1startIndex, b1length, temp);
        int currPos     = temp[0];
        int valSize     = temp[3];
        
        return bytesToLong(buff, currPos - valSize + b2startIndex, valSize);
    }
    
    static private byte[][] addValue(long v, int addSize, byte masterByte, byte[] statusBytes, byte[] buff)
    {
        byte[] valBuff = longToBytes(v);
        byte[][] b = resetStatusBytes(valBuff.length, addSize, masterByte, statusBytes);
        
        // set value in buff
        if (buff == null) 
        {
            buff = valBuff;
        }
        else
        {
            byte[] newBuff = new byte[buff.length + valBuff.length];
            System.arraycopy(buff, 0, newBuff, 0, buff.length);
            System.arraycopy(valBuff, 0, newBuff, buff.length, valBuff.length);
            buff = newBuff;
        }
        
        byte[][] ret = new byte[3][];
        ret[0] = b[0];
        ret[1] = b[1];
        ret[2] = buff;
        return ret;
    }
    
    /**
     * Returns current available position for the next value in buff and also sets size of the new value 
     */
    static private byte[][] resetStatusBytes(int newSize, int addSize, byte masterByte, byte[] statusBytes)
    {
        // get number of current values added so far
        int size = ((0xFF & masterByte) >> 3);
        if (addSize > 0)
            size += addSize;
        
        int[] temp = new int[4];
        getPosIndices(size - 1, statusBytes, 0, statusBytes==null?0:statusBytes.length, temp);
        int byteIndex   = temp[1];
        int bitIndex    = temp[2];
        
        if (statusBytes == null || statusBytes.length == 0) 
        {
            statusBytes = new byte[1];
            statusBytes[0] = 0;
        }
         
        int maxIndex;
        if (bitIndex > 5) 
            maxIndex = byteIndex + 1;
        else
            maxIndex = byteIndex;
        if (maxIndex >= statusBytes.length)
        {
            byte[] newBuff = new byte[statusBytes.length + 1];
            System.arraycopy(statusBytes, 0, newBuff, 0, statusBytes.length);
            newBuff[statusBytes.length] = 0;
            statusBytes = newBuff;
        }
        
        int byte0 = statusBytes[byteIndex] & 0xFF;
        int byte1 = 0;
        if (byteIndex + 1 < statusBytes.length) byte1 = statusBytes[byteIndex + 1] & 0xFF;
        int joint = byte0 | (byte1 << 8);
        joint = setBits(joint, (newSize - 1), bitIndex);
        
        statusBytes[byteIndex] = (byte) (joint & 0xFF);
        if (maxIndex == byteIndex + 1)
        {
            statusBytes[byteIndex+1] = (byte) (joint >> 8);
        }
        
        if (addSize == -1)
        {
            // increment masterByte count
            size++;
            masterByte = (byte) ((masterByte & 0x7) | (size << 3));
        }
        
        byte[][] ret = new byte[2][];
        ret[0] = new byte[] { masterByte };
        ret[1] = statusBytes;
        return ret;
    }
    
    static private int setBits(int target, int val, int bitIndex)
    {
        int mask = ~(0x7 << bitIndex);
        return (target & mask) | (val << bitIndex);
    }
    
    /**
     * valSize - is the size of the value at index location
     */
    static private void getPosIndices(int index, byte[] buff, int startIndex, int length, int[] result)
    {
        int byteIndex = 0;
        int bitIndex = 0;
        int currPos = 0;
        int valSize = 0;
        for (int i = 0; i <= index; i++)
        {
            int byte0 = buff[byteIndex+startIndex] & 0xFF;
            int byte1 = 0;
            if (byteIndex + 1 < length) byte1 = buff[byteIndex + 1 + startIndex] & 0xFF;
            int joint = byte0 | (byte1 << 8);
            valSize = ((joint >> bitIndex) & 0x7) + 1;
            currPos += valSize;

            bitIndex += 3;
            if (bitIndex > 7)
            {
                byteIndex++;
                bitIndex -= 8;
            }
        }
        
        result[0] = currPos;
        result[1] = byteIndex;
        result[2] = bitIndex;
        result[3] = valSize;
    }
    
    static private byte[] longToBytes(long v)
    {
        int maxSize = 8;
        
        long num = v;
        int i;
        for (i = 1; i < maxSize; i++)
        {
            num = num >> 7;
            if (v < 0)
            {
                if (num == -1) break;
            }
            else
            {
                if (num == 0) break;
            }
            num = num >> 1;
        }
        
        int size = i;
        byte [] valBuff = new byte[size];
        num = v;
        for (i = 0; i < size; i++)
        {
            valBuff[i] = (byte) (num & 0xFF);
            num = num >> 8;
        }
        
        return valBuff;
    }
    
//  static private long bytesToLong(byte[] buff, int size)
//  {
//      long mask = 0xFF;
//      long res = 0;
//      
//      int i;
//      for (i = 0; i < size - 1; i++)
//      {
//          res += ((mask & buff[i]) << (i * 8));
//      }
//      
//      res += (((long) buff[i]) << (i * 8));
//          
//      return res;
//  }
//  
    static private long bytesToLong(byte[] buff, int index, int size)
    {
        long mask = 0xFF;
        long res = 0;
        
        int i;
        for (i = 0; i < size - 1; i++)
        {
            res += ((mask & buff[i + index]) << (i * 8));
        }
        
        res += (((long) buff[i + index]) << (i * 8));
            
        return res;
    }
    
    // NOTE: cast to INT is essential for replicating sign
//  static private int getIntFor(byte[] buff)
//  {
//      int mask = 0xFF;
//      return (mask & buff[0]) + (((int) buff[1]) << 8);
//  }
//
    // NOTE: cast to INT is essential for replicating sign
    static private int getIntFor(byte[] buff, int startIndex)
    {
        int mask = 0xFF;
        return (mask & buff[startIndex]) + (((int) buff[startIndex+1]) << 8);
    }

    static private byte[] getBytesFor(int num)
    {
        byte[] buff = new byte[2];
        buff[0] = (byte) (num & 0xFF);
        num = num >> 8;
        buff[1] = (byte) (num & 0xFF);
        return buff;    
    }
    
    
    
    //-----------------------------------------------------------------------
    // alexg: conversion to and from binary stream for migration to TxNode55 
    //-----------------------------------------------------------------------
    
//  static public byte[] toByteArray55(TxNode node)
//  {   
//      return toByteArray55(node, null, HEADER_55);
//  }
//  
//  static public byte[] toByteArray55(TxNode node, String encoding)
//  {
//      return toByteArray55(node, encoding, HEADER_55);
//  }

    static private byte[] toByteArray55(TxNode node, String encoding, int header, int ver)
    {
        short nValues = 0;
        short valuesBuffOffset = 0;
        
        // allocate buffer assuming that value will take 4 bytes on average
        byte[] valuesBuff = new byte[node.getValuesCount() * 4];
        
        for (int i = 0; i < node.getValuesCount(); i++)
        {
            long value = node.valueAt(i);
            
            nValues++;
            if (valuesBuff == null || valuesBuff.length < valuesBuffOffset + DataUtil.getValueLength(value))
            {
                byte[] temp = new byte[valuesBuffOffset + 20];
                if (valuesBuff != null)
                {
                    System.arraycopy(valuesBuff, 0, temp, 0, valuesBuffOffset);
                }
                valuesBuff = temp;
            }
            valuesBuffOffset += DataUtil.writeValue(valuesBuff, value, valuesBuffOffset);

        }
        
        int size = 0;
        if (ver == VERSION_55)
        {
            size = 1 + DataUtil.getValueLength(valuesBuffOffset) + valuesBuffOffset;    // 1 byte for nValues
        }
        else if (ver == VERSION_551)
        {
            size = DataUtil.getValueLength(nValues) + DataUtil.getValueLength(valuesBuffOffset) + valuesBuffOffset;
        }
        
        int nMsgs = 0;
        int len = 0;
        byte[][] msgBuff = null;
        if (node.msgs != null)
        {
            nMsgs = node.msgs.size();
            msgBuff = new byte[nMsgs][];
            for (int i = 0; i < nMsgs; i++)
            {
                String str = (String) node.msgs.elementAt(i);
                try
                {
                    if (str != null)
                    {
                        if (encoding == null)
                        {
                            msgBuff[i] = str.getBytes();
                        }
                        else
                        {
                            msgBuff[i] = str.getBytes(encoding);
                        }
                    }
                    else
                    {
                        msgBuff[i] = new byte[0];
                    }
                }
                catch(Exception e)
                {
                    msgBuff[i] = new byte[0];
                    e.printStackTrace();
                }

                len += (msgBuff[i].length + DataUtil.getValueLength(msgBuff[i].length));
            }
            
        }
        
        size += (DataUtil.getValueLength(nMsgs) + len);
        
        int nChildren = 0;
        len = 0;
        byte[][] childrenBuff = null;
        if (node.children != null)
        {
            nChildren = node.children.size();
            childrenBuff = new byte[nChildren][];
            for (int i = 0; i < nChildren; i++)
            {
                TxNode child = (TxNode) node.children.elementAt(i);
                if (child != null)
                {
                    childrenBuff[i] = toByteArray55(child, encoding, -1, ver);
                }
                else
                {
                    childrenBuff[i] = new byte[0];
                }
                
                len += (childrenBuff[i].length + DataUtil.getValueLength(childrenBuff[i].length));
            }
        }
        
        size += (DataUtil.getValueLength(nChildren) + len);
        
        int binLen = 0;
        if (node.binData != null)
        {
            binLen = node.binData.length;
        }

        size += (DataUtil.getValueLength(binLen) + binLen);

        if (header == HEADER_55 || header == HEADER_551) size += 4;
        
        byte[] buff = new byte[size];
        
        int offset = 0;
        if (header == HEADER_55 || header == HEADER_551) 
        {
            DataUtil.writeInt(buff, header, offset);
            offset += 4;
        }
                
        // do values
        if (ver == VERSION_55)
        {
            buff[offset++] = (byte) nValues;
        }
        else if (ver == VERSION_551)
        {
            offset += DataUtil.writeValue(buff, nValues, offset);
        }
        
        offset += DataUtil.writeValue(buff, valuesBuffOffset, offset);
        System.arraycopy(valuesBuff, 0, buff, offset, valuesBuffOffset);
        offset += valuesBuffOffset;
        
        // do msgs
        offset += DataUtil.writeValue(buff, nMsgs, offset);
        for (int i = 0; i < nMsgs; i++)
        {
            offset += DataUtil.writeValue(buff, msgBuff[i].length, offset);
            System.arraycopy(msgBuff[i], 0, buff, offset, msgBuff[i].length);
            offset += msgBuff[i].length;            
        }
        
        // do children
        offset += DataUtil.writeValue(buff, nChildren, offset);
        for (int i = 0; i < nChildren; i++)
        {
            offset += DataUtil.writeValue(buff, childrenBuff[i].length, offset);
            System.arraycopy(childrenBuff[i], 0, buff, offset, childrenBuff[i].length);
            offset += childrenBuff[i].length;           
        }
        
        // do binData
        offset += DataUtil.writeValue(buff, binLen, offset);
        if (node.binData != null)
        {
            System.arraycopy(node.binData, 0, buff, offset, node.binData.length);
            offset += node.binData.length;
        }
        
        // System.out.println(buff.length+" == "+offset);
        
        return buff;
    }

    static private TxNode fromByteArray55(byte[] buff, int offsetInt, String encoding, int ver)
    {
//      System.out.println("TxNode::fromByteArray55");

        TxNode node = new TxNode();
        node.setVersion(ver);

        if (buff == null || buff.length == 0) return node;

        int[] offset = new int[] { offsetInt };
        short nValues = 0;
        if (ver == VERSION_55)
        {
            nValues = buff[offset[0]++];
        }
        else if (ver == VERSION_551)
        {
            nValues = (short) DataUtil.readValue(buff, offset);
        }
        
        short valuesBuffOffset = (short) DataUtil.readValue(buff, offset);

//      System.out.println("nValues: "+nValues+", "+ver);
//      System.out.println("valuesBuffOffset: "+node.valuesBuffOffset);
        
        byte[] valuesBuff = new byte[valuesBuffOffset];
        System.arraycopy(buff, offset[0], valuesBuff, 0, valuesBuffOffset);
        offset[0] += valuesBuffOffset;

        // add values
        long val = 0;
        int[] offsetVal = new int[] { 0 };
        for (int i = 0; i < nValues; i++)
        {
            val = DataUtil.readValue(valuesBuff, offsetVal);
            node.addValue(val);
        }
        
        // do msgs
        int nMsgs = (int) DataUtil.readValue(buff, offset);
        
//      System.out.println("nMsgs: "+nMsgs);
        
        for (int i = 0; i < nMsgs; i++)
        {
            int msgLen = (int) DataUtil.readValue(buff, offset);
            if (msgLen >= 0)
            {
                String str = "";
                try
                {
                    if (encoding == null)
                    {
                        str = new String(buff, offset[0], msgLen);
                    }
                    else
                    {
                        str = new String(buff, offset[0], msgLen, encoding);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                offset[0] += msgLen;
                node.addMsg(str);
            }
            else
            {
                node.addMsg(null);              
            }           
        }
        
        // do children
        int nChildren = (int) DataUtil.readValue(buff, offset);
        
//      System.out.println("nChildren: "+nChildren);
        
        for (int i = 0; i < nChildren; i++)
        {
            int childLen = (int) DataUtil.readValue(buff, offset);
            TxNode n = fromByteArray55(buff, offset[0], encoding, ver);
            
            offset[0] += childLen;
            
            node.addChild(n);
        }
        
        // do binData
        int binLen = (int) DataUtil.readValue(buff, offset);
        
//      System.out.println("binLen: "+binLen);

        if (binLen > 0)
        {
            node.binData = new byte[binLen];
            System.arraycopy(buff, offset[0], node.binData, 0, binLen);
        }
        
        return node;

    }

    //-----------------------------------------------------------------------------
    // alexg: conversion to and from binary stream for migration to TxNode55 - done
    //-----------------------------------------------------------------------------
    
    
    
    
    
    
    
    
    //SKIP_SEGMENT_START
    /**
     * Helper function to use on the server - sets isExpandChildren recursively, but only on the
     * node children (if the child is in binary form, it skips it)
     */
    public void setIsExpandChildrenToAll(boolean b)
    {
        this.isExpandChildren = b;
        for (int i = 0; this.children != null && i < this.children.size(); i++)
        {
            Object obj = this.children.elementAt(i);
            if (obj instanceof TxNode)
            {
                ((TxNode) obj).setIsExpandChildrenToAll(b);
            }
        }
    }
    
    static private int equals(TxNode n0, TxNode n1)
    {
        if (n0 == null && n1 == null)
        {
            return 0;
        }
        else
        {
            if (n0 == null || n1 == null) return 10;
        }
        
        if (n0.masterByte != n1.masterByte) return 20;
        int r = equalsBin(n0.statusBytes, n1.statusBytes);
        if (r != 0) return 30 + r;
        r = equalsBin(n0.buff, n1.buff);
        if (r != 0) return 40 + r;
        r = equalsBin(n0.binData, n1.binData);
        if (r != 0) return 50 + r;
        
        if (n0.msgs != null && n1.msgs != null)
        {
            if (n0.msgs.size() != n1.msgs.size()) return 60;
            for (int i = 0; i < n0.msgs.size(); i++)
            {
                String s0 = (String) n0.msgs.elementAt(i);
                String s1 = (String) n1.msgs.elementAt(i);
                if (!(s0).equals(s1)) return 70;
            }
        }
        else
        {
            if (n0.msgs != null || n1.msgs != null) return 80;
        }
        
        if (n0.children != null && n1.children != null)
        {
            if (n0.children.size() != n1.children.size()) return 90;
            for (int i = 0; i < n0.children.size(); i++)
            {
                Object obj0 = n0.children.elementAt(i);
                Object obj1 = n1.children.elementAt(i);
                TxNode nn0, nn1;
                if (obj0 instanceof TxNode)
                    nn0 = (TxNode) obj0;
                else
                    nn0 = TxNode.fromByteArray50((byte[]) obj0, 0, null, VERSION_50);
                
                if (obj1 instanceof TxNode)
                    nn1 = (TxNode) obj1;
                else
                    nn1 = TxNode.fromByteArray50((byte[]) obj1, 0, null, VERSION_50);
                    
                r = equals(nn0, nn1);
                if (r != 0) return 100 + r; 
            }
        }
        else
        {
            if (n0.children != null || n1.children != null) return 130;
        }
        
        return 0;
    }
    
    static private int equalsBin(byte[] b0, byte[] b1)
    {
        if (b0 != null && b1 != null)
        {
            if (b0.length != b1.length) return 1;
            for (int i = 0; i < b0.length; i++)
            {
                if (b0[i] != b1[i]) return 2;
            }
        }
        else
        {
            if (b0 != null || b1 != null) return 3;
        }
        return 0;
    }
    
    static private TxNode createNode(int nLayers)
    {
        TxNode n = new TxNode();
        
        addValues(n);
        addMsgs(n);
        addBinData(n);
        addChildren(n, nLayers);
        return n;
    }
    
    static private void addChildren(TxNode n, int nLayers)
    {
        if (nLayers == 0) return;
        
        for (int i = 0; i < 5; i++)
        {
            if (i == 3)
                n.addChild(null);
            else
                n.addChild(createNode(nLayers-1));
        }
    }
    
    static private void addValues(TxNode n)
    {
        n.addValue(127);
        n.addValue(8234567890123456789L);
        n.addValue(0xFFFF / 2);
        n.addValue(0xFFFF / 2 + 1);
        n.addValue(-8234567890123456789L);
    }
    
    static private void addMsgs(TxNode n)
    {
        for (int j = 0; j < 70; j++)
        {
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < 3; i++) buff.append("-");
            n.addMsg(buff.toString());
        }
    }
    
    static private void addBinData(TxNode n)
    {
        int size = 60;
        n.binData = new byte[size];
        for (int i = 0; i < size; i++) n.binData[i] = (byte) (i + 10);
    }
    
    public String toString()
    {
        return toString(0);
    }
    
    private String toString(int level)
    {
        StringBuffer buf = new StringBuffer();
        StringBuffer prefix = new StringBuffer();
        for (int i = 0; i < level; i++) prefix.append("\t");
        
        String offset = prefix.toString();
        
        try
        {

            buf.append(offset + "------------ TxNode -----------\n");
            buf.append(offset + "Values:\n");
            for (int i = 0; i < getValuesCount(); i++)
            {
                buf.append(offset + "[" + i + "]=" + valueAt(i) + "\n");
            }
            
            boolean binaryExist = (binData != null && binData.length != 0);
            buf.append(offset + "binary exists ? = " + binaryExist);
            if( binaryExist )
            {
                buf.append(", length = "+binData.length);
            }
            buf.append("\n");
                    
            if (msgs != null && msgs.size() > 0)
            {
                buf.append(offset + "------------ messages -----------\n");
                for (int j = 0; j < msgs.size(); j++)
                    buf.append(offset + String.valueOf(msgs.elementAt(j)) + "\n");
            }
            if (children != null && children.size() > 0)
            {
                buf.append(offset + "------------ children -----------\n"); 
                for (int j = 0; children != null && j < children.size(); j++)
                {
                    TxNode child = (TxNode)children.elementAt(j);
                    if (child != null) buf.append(child.toString(level+1) + "\n");
                    else buf.append(offset + "\tNULL CHILD\n");
                }
            }
            buf.append(offset + "------------ end of TxNode ------\n");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return buf.toString();
    }

    //SKIP_SEGMENT_END
}