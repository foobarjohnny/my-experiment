
public class GenerateVerificationCode
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		GenerateVerificationCode test = new GenerateVerificationCode();
		test.testPTNVerification();
	}

    
    public void testPTNVerification()
    {
    	String code = generatePTNVerificationCode("3817799999",6);
    	System.out.println("code:" + code);
    }
    
    private  String generatePTNVerificationCode(String ptn, int length)
    {
        long ptnL = Long.parseLong(ptn);

        // a prime number, not too small.
        long mainDisturbance = 19843L;

        // second prime number
        long secondDisturbance = 751L;
        final long tenThousand = 10000L;
        long middleResult = ((ptnL / (tenThousand * tenThousand)) * mainDisturbance)
                + ((ptnL / tenThousand) * secondDisturbance) + ((ptnL % tenThousand) * mainDisturbance)
                + (secondDisturbance * mainDisturbance);

        int refValue = 1;
        for (int i = 0; i < length; i++)
            refValue = refValue * 10;
        while (middleResult >= refValue)
        {
            middleResult = (middleResult % refValue) + (middleResult / refValue);
        }

        String resultPin = middleResult + "";

        if (resultPin.length() < length)
        {
            resultPin = resultPin + ptn.substring(4, ((4 + length) - resultPin.length()));
        }

        return resultPin;
    }
}
