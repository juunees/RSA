
package Prime_Test;

import java.math.BigInteger;
import java.util.Random;


/**
 * Created by junes on 18.10.16.
 */

public class MillerRabin_Test {

    private BigInteger _mNUMBER;    // проверяемое число
    private int _mk;                // точность столько раз проверяем
    private static final BigInteger TWO = new BigInteger("2");


    private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) {

        Random rnd = new Random();
        BigInteger res;
        do {
            res = new BigInteger(top.bitLength(), rnd);
        } while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);

        return res;
    }

    public MillerRabin_Test(BigInteger _x, int k) {
        this._mNUMBER = _x;
        this._mk = k;
    }


    public boolean isProbablePrime() {

        BigInteger d = _mNUMBER.subtract(BigInteger.ONE);

        int s = 0;
        while (d.mod(TWO).equals(BigInteger.ZERO)) {
            d = d.divide(TWO);
            s++;
        }

        for (int j = 0; j < _mk; j++) {

            BigInteger a = uniformRandom(TWO, _mNUMBER.subtract(BigInteger.ONE));

           Power_Montgomery M = new Power_Montgomery(_mNUMBER);
            BigInteger xm = M.convertIn(a);
            BigInteger zm = M.pow_my(xm,d);
            BigInteger x = M.convertOut(zm);

           // BigInteger x = a.modPow(d,_mNUMBER);

            if (x.equals(BigInteger.ONE) || x.equals(_mNUMBER.subtract(BigInteger.ONE)))
                continue;
            int r = 1;
            for (; r < s ; r++) {
                x = x.modPow(TWO, _mNUMBER);
                if (x.equals(BigInteger.ONE))
                    return false;
                if (x.equals(_mNUMBER.subtract(BigInteger.ONE)))
                    break;
            }
            if (r == s)
                return false;
        }

        return true;

    }


    public static void main(String[] args) {

        MillerRabin_Test MR = new MillerRabin_Test(BigInteger.valueOf(9901),3);
        System.out.println(MR.isProbablePrime());

    }

}
