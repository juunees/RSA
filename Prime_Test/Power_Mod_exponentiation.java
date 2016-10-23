package Prime_Test;

import java.math.BigInteger;

/**
 * Created by junes on 22.10.16.
 */
public class Power_Mod_exponentiation {

    private BigInteger base;
    private BigInteger exponent;
    private BigInteger modulus;

    public Power_Mod_exponentiation(BigInteger b, BigInteger e, BigInteger m){
        this.base = b;
        this.exponent = e;
        this.modulus = m;
    }

    public BigInteger modular_pow(){

        if(modulus.equals(BigInteger.ONE)) return BigInteger.ZERO;
        assert base.signum() >= 0 && base.compareTo(modulus) < 0;

        BigInteger result = BigInteger.ONE;

        char [] mas_exp = exponent.toString(2).toCharArray();

        int len = mas_exp.length;
        BigInteger [] mas_res = new BigInteger[len];

        mas_res[len-1] = base.mod(modulus);

        for(int i = len-1 ; i > 0 ; --i){
            mas_res[i-1] = mas_res[i].multiply(mas_res[i]).mod(modulus);
        }

        for(int i = len; i > 0 ; ){
            if(mas_exp[--i]=='1')result = result.multiply(mas_res[i]).mod(modulus);
        }

        return result;
    }


    public static void main(String []args){


        long startTime = System.currentTimeMillis();

        //Power_Montgomery M = new Power_Montgomery(BigInteger.valueOf(221));
        BigInteger xm = BigInteger.valueOf(256);
        BigInteger yb = BigInteger.valueOf(110);
        //BigInteger zm = M.pow(xm, yb);
        BigInteger actual = xm.modPow(yb,BigInteger.valueOf(597));

        System.out.println(actual);

        long timeSpent = System.currentTimeMillis() - startTime;

        System.out.println("программа(быстрое возведение в степень) выполнялась " + timeSpent + " миллисекунд");


    }

}
