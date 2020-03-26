package com.jvm.methodinvoke;

/**
 * @author xiaoyiluo
 * @createTime 2018/10/14 20:55
 **/
public class BridgeMethod {


    interface Customer {
        boolean isVIP();
    }


    class VIP implements Customer{
        @Override
        public boolean isVIP() {
            return false;
        }
    }

    class Merchant {
        public Number actionPrice(double price, Customer customer) {
            return null;
        }
    }

    class NaiveMerchant extends Merchant {
        @Override
        public Double actionPrice(double price, Customer customer) {
            return null;
        }
    }

    class Merchant2<T extends Customer> {
        public double actionPrice(double price, T customer) {
            return 0;
        }
    }

    class VIPOnlyMerchant extends Merchant2<VIP> {
        @Override
        public double actionPrice(double price, VIP customer) {
            return 0;
        }
    }
}
