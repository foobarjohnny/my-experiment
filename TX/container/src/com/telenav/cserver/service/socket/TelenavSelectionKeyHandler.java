/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.telenav.cserver.service.socket;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.grizzly.BaseSelectionKeyHandler;
import com.sun.grizzly.SelectorHandler;
import com.sun.grizzly.util.Copyable;
import com.sun.grizzly.util.SelectionKeyAttachment;

/**
 *
 * @author yueyulin Jul 14, 2008
 */
public class TelenavSelectionKeyHandler extends BaseSelectionKeyHandler  {

	private static Logger logger = Logger.getLogger(TelenavSelectionKeyHandler.class.getName());

    /**
     * Next time the exprireKeys() will delete keys.
     */
    protected long nextKeysExpiration = 0;
    
    
    /*
     * Number of seconds before idle keep-alive connections expire
     */
    protected long timeout = 15 * 1000L;

    
    public TelenavSelectionKeyHandler() {
    }
   
    
    public TelenavSelectionKeyHandler(SelectorHandler selectorHandler) {
        super(selectorHandler);
    }

   
    /**
     * {@inheritDoc}
     */
    @Override
    public void process(SelectionKey key) {
        super.process(key);
        removeExpirationStamp(key);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcess(SelectionKey key) {
        super.postProcess(key);
        addExpirationStamp(key);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Iterator<SelectionKey> keyIterator, int selectionKeyOps) {
        long currentTime = System.currentTimeMillis();
        SelectionKey key;
        while (keyIterator.hasNext()) {
            key = keyIterator.next();
            keyIterator.remove();
            doRegisterKey(key, selectionKeyOps, currentTime);
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void register(SelectionKey key, int selectionKeyOps) {
        doRegisterKey(key, selectionKeyOps, System.currentTimeMillis());
    }
    
    /**
     * Registers <code>SelectionKey</code> to handle certain operations
     */
    protected void doRegisterKey(SelectionKey key, int selectionKeyOps,
            long currentTime) {
        if (!key.isValid()) {
            return;
        }

        key.interestOps(key.interestOps() | selectionKeyOps);
        Object attachment = key.attachment();
        // By default, attachment a null.
        if (attachment == null) {
            key.attach(currentTime);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(SelectableChannel channel, int ops) 
            throws ClosedChannelException {
        if (!channel.isOpen()) {
            return;
        }

        Selector selector = selectorHandler.getSelector();
        SelectionKey key = channel.keyFor(selector);
        long time = System.currentTimeMillis();
        
        if (key == null) {
            key = channel.register(selector, ops, time);
        } else {
            doRegisterKey(key, ops, time);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void register(SelectionKey key, long currentTime){
       ;
    }
    
    
    /**
     * @deprecated
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void expire(SelectionKey key, long currentTime) {
        ;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void expire(Iterator<SelectionKey> iterator) {
        if (timeout <= 0) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextKeysExpiration) {
            return;
        }
        nextKeysExpiration = currentTime + timeout;        
                
        
        SelectionKey key;
        while (iterator.hasNext()) {
            key = iterator.next();
            
            if (!key.isValid()){
                continue;
            }


            Long expire = getExpirationStamp(key);
            if (expire != null){
                if (currentTime - expire >= timeout) {
                    logger.warning("expire and cancel"+key.channel());
                    cancel(key);
                } else if (expire + timeout < nextKeysExpiration) {
                    nextKeysExpiration = expire + timeout;
                }
            }
        }
    }
    

    @Override
    public void copyTo(Copyable copy) {
    	super.copyTo(copy);
    	TelenavSelectionKeyHandler copyHandler = (TelenavSelectionKeyHandler) copy;
        copyHandler.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }
    
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    

    @Override
    protected Object clearKeyAttachment(SelectionKey key) {
        Object attachment = super.clearKeyAttachment(key);
        if (attachment instanceof SelectionKeyAttachment) {
            ((SelectionKeyAttachment) attachment).release(key);
        }
        return attachment;
    }


    /**
     * Removes expiration timeout stamp from the <code>SelectionKey</code> 
     * depending on its attachment
     * 
     * @param <code>SelectionKey</code>
     */
    private void removeExpirationStamp(SelectionKey key) {
        Object attachment = key.attachment();
        if (attachment != null) {
            if (attachment instanceof Long) {
                key.attach(null);
            } else if (attachment instanceof SelectionKeyAttachment) {
                ((SelectionKeyAttachment) attachment).setTimeout(null);
            }
        }
    }
    
    /**
     * Adds expiration timeout stamp to the <code>SelectionKey</code> 
     * depending on its attachment
     * 
     * @param <code>SelectionKey</code>
     */
    private void addExpirationStamp(SelectionKey key) {
        long currentTime = System.currentTimeMillis();
        Object attachment = key.attachment();
        if (attachment == null) {
            key.attach(currentTime);
        } else if (attachment instanceof SelectionKeyAttachment) {
            ((SelectionKeyAttachment) attachment).setTimeout(currentTime);
        }    
    }
    
    /**
     * Gets expiration timeout stamp from the <code>SelectionKey</code> 
     * depending on its attachment
     * 
     * @param <code>SelectionKey</code>
     */
    private Long getExpirationStamp(SelectionKey key) {
        Object attachment = key.attachment();
        if (attachment != null) {
            try {

                // This is extremely bad to invoke instanceof here but 
                // since the framework expose the SelectionKey, an application
                // can always attach an object on the SelectionKey and we 
                // can't predict the type of the attached object.                                
                if (attachment instanceof Long) {
                    return (Long) attachment;
                } else if (attachment instanceof SelectionKeyAttachment) {
                    return ((SelectionKeyAttachment) attachment).getTimeout();
                }
            } catch (ClassCastException ex) {
    			logger.log(Level.SEVERE, "Invalid SelectionKey attachment", ex);
            }
        }

        return null;
    }

}
