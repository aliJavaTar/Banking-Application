//package org.bro.banking.domin.account.usecase;
//
//import java.math.BigInteger;
//
//public class Three implements Runnable {
//    private final String codeBank;
//
//    public Three(String codeBank) {
//        this.codeBank = codeBank;
//    }
//
//    @Override
//    public void run() {
//        System.out.println("______Three_________Three____________________");
//
//        boolean flag;
//        BigInteger count = BigInteger.ZERO;
//        BigInteger baseNumber = new BigInteger(codeBank + "000000000000");
//
//        for (BigInteger index = BigInteger.TEN.pow(8); index.compareTo(BigInteger.TEN.pow(12)) < 0; index = index.add(BigInteger.ONE)) {
//            BigInteger cardNumber = baseNumber.add(index);
//
//            flag = validationCardNumber(cardNumber.toString());
//            if (flag)
//            {
//                count = count.add(BigInteger.ONE);
//                System.out.println(count+"  ::::::::::::::::::   Three");
//            }
//
//        }
//        System.out.println("Three is done " + count);
//    }
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
//
//}