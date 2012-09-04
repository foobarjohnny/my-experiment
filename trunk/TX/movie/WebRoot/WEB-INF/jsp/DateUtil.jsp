<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>

<tml:script language="fscript" version="1">
		<![CDATA[
		    func getDateStr(int dateIndex)
        		int time = Time.get()
				time = time + dateIndex*86400000
				String result = Time.format("yyyy-MM-dd", time)
				return result;		    	
		    endfunc
		]]>
</tml:script>