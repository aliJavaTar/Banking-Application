//package org.bro.banking.domin.account.usecase;
//
//import java.math.BigInteger;
//
//public class Two implements Runnable{
//    private final String codeBank;
//
//    public Two(String codeBank) {
//        this.codeBank = codeBank;
//    }
//
//    @Override
//    public void run() {
//        System.out.println("______Two_________Two____________________");
//        boolean flag;
//        BigInteger count = BigInteger.ZERO;
//        BigInteger baseNumber = new BigInteger(codeBank + "000000000000");
//
//        for (BigInteger index =BigInteger.TEN.pow(4); index.compareTo(BigInteger.TEN.pow(8)) < 0; index = index.add(BigInteger.ONE)) {
//            BigInteger cardNumber = baseNumber.add(index);
//
//
//            flag = validationCardNumber(cardNumber.toString());
//            if (flag)
//            {
//                count = count.add(BigInteger.ONE);
//                System.out.println(count+"  ::::::::::::::::::   Two");
//            }
//
//        }
//        System.out.println("Two is done " + count);
//
//
//    }
//
//    boolean validationCardNumber(String cardNo) {
//        int nDigits = cardNo.length();
//
//        int nSum = 0;
//        boolean isSecond = false;
//        for (int index = nDigits - 1; index >= 0; index--) {
//
//            int d = cardNo.charAt(index) - '0';
//
//            if (isSecond)
//                d = d * 2;
//
//            nSum += d / 10;
//            nSum += d % 10;
//
//            isSecond = !isSecond;
//        }
//        return (nSum % 10 == 0);
//    }
//}
