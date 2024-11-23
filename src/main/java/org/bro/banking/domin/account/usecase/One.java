package org.bro.banking.domin.account.usecase;

public class One implements Runnable {
    private final String codeBank;
    private final long start;
    private final long end;

    public One(String codeBank, long start, long end) {
        this.codeBank = codeBank;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        System.out.println("______ONE_________ONE____________________");
        boolean flag;
        long count = 0;
        long baseNumber = 6037000000000000L;

        for (long index = start; index < end; index++) {
            long cardNumber = baseNumber + index;

//            System.out.println("this is our index : IN CLASS ONE : " + index);
//            System.out.println(start + "______" + end);
//            System.out.println("sldfjladsjfkl jajsl");
//            System.out.println("sldfjladsjfkl jajsl");
//            System.out.println("sldfjladsjfkl jajsl");
//            System.out.println("sldfjladsjfkl jajsl");
//            System.out.println("sldfjladsjfkl jajsl");
//            System.out.println("sldfjladsjfkl jajsl");
            flag = validationCardNumber(String.valueOf(cardNumber));
            if (flag) {
                count++;
//                System.out.println(count + "  ::::::::::::::::::   ONE");
            }
        }
        System.out.println(count);
        System.out.println("one is done " + count);


    }

    boolean validationCardNumber(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int index = nDigits - 1; index >= 0; index--) {

            int d = cardNo.charAt(index) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        var result = (nSum % 10 == 0);
        if (result)
            System.out.println("is amin test : " + cardNo);
        return result;
    }
}
