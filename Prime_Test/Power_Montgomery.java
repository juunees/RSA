package Prime_Test;

import java.math.BigInteger;



public class Power_Montgomery{

    private BigInteger modulus; //  Input parameter

    private BigInteger reducer;       // R =  2 ^    Is a power of 2
    private int reducerBits;          // Степень 2 в R ^ которая равна длине числа в битах
    private BigInteger reciprocal;    //  reducer^-1 mod modulus
    private BigInteger mask;          // Because x mod reducer = x & (reducer - 1)
    private BigInteger factor;        // Equal to (reducer * reducer^-1 - 1) / n
    private BigInteger convertedOne;


    public Power_Montgomery(BigInteger modulus) {

        if (modulus == null)
            throw new NullPointerException();

        if (!modulus.testBit(0)|| modulus.compareTo(BigInteger.ONE) <= 0)
            throw new IllegalArgumentException("Modulus must be an odd number at least 3");

        this.modulus = modulus;

        // Reducer
        reducerBits = (modulus.bitLength() / 8 + 1) * 8;      //  длина числа в битах
        reducer = BigInteger.ONE.shiftLeft(reducerBits);     // 1 c количесвом 0 = длине
        mask = reducer.subtract(BigInteger.ONE);
        assert reducer.compareTo(modulus) > 0 && reducer.gcd(modulus).equals(BigInteger.ONE);

        // Other computed numbers
        reciprocal = reducer.modInverse(modulus);    //  R^(-1) mod N
        factor = reducer.multiply(reciprocal).subtract(BigInteger.ONE).divide(modulus);    // (R*R^(-1) - 1)/N
        convertedOne = reducer.mod(modulus);

    }



    public BigInteger multiply(BigInteger x, BigInteger y) {

        assert x.signum() >= 0 && x.compareTo(modulus) < 0;   // x < 0   x > modul
        assert y.signum() >= 0 && y.compareTo(modulus) < 0;


        BigInteger product = x.multiply(y);

        // поразрядное нахождение K = temp
        BigInteger temp = product.and(mask).multiply(factor).and(mask);

        // T:= (X+KN)/R. (Деление на R выполняется путем сдвига на соответствующее количество разрядов).
        BigInteger reduced = product.add(temp.multiply(modulus)).shiftRight(reducerBits);

        BigInteger result = reduced.compareTo(modulus) < 0 ? reduced : reduced.subtract(modulus);
        assert result.signum() >= 0 && result.compareTo(modulus) < 0;

        return result;



    }

    public BigInteger convertIn(BigInteger x) {
        return x.shiftLeft(reducerBits).mod(modulus);
    }


    // The range of x is unlimited
    public BigInteger convertOut(BigInteger x) {
        return x.multiply(reciprocal).mod(modulus);
    }


    public BigInteger pow_my(BigInteger x, BigInteger y) {

        assert x.signum() >= 0 && x.compareTo(modulus) < 0;
        if (y.signum() == -1)
            throw new IllegalArgumentException("Negative exponent");

        BigInteger z = convertedOne;
        for (int i = 0, len = y.bitLength(); i < len; i++) {
            if (y.testBit(i))
                z = multiply(z, x);
            x = multiply(x, x);
        }
        return z;
    }

    public static void main(String []args){


      long startTime = System.currentTimeMillis();



      // long timeSpent = System.currentTimeMillis() - startTime;

       // System.out.println("программа(метод Монтгомери) выполнялась " + timeSpent + " миллисекунд");
        BigInteger mod = BigInteger.valueOf(3659);
        BigInteger x = BigInteger.valueOf(1204);
        BigInteger y = BigInteger.valueOf(57);


       Power_Montgomery red = new Power_Montgomery(mod);
        BigInteger xm = red.convertIn(x);
        BigInteger zm;
       // BigInteger z;

        zm = red.pow_my(xm, y);
        //z = x.modPow(y, mod);

        BigInteger res = red.convertOut(zm);

        System.out.println("Мотнгомери " + res);
       // System.out.println("Быстрое  " + z);
        long timeSpent = System.currentTimeMillis() - startTime;


        System.out.println("программа(метод Монтгомери) выполнялась " + timeSpent + " миллисекунд");
    }

}
