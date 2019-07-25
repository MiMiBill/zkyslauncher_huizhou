package com.muju.note.launcher.app.orderfood.bean;

import android.util.Log;

import com.muju.note.launcher.app.orderfood.db.ComfoodDao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheng on 16-11-12.
 */
public class ShopCart {
    private int shoppingAccount;//商品总数
    private double shoppingTotalPrice;//商品总价钱
    private Map<ComfoodDao,Integer> shoppingSingle;//单个物品的总价价钱

    public ShopCart(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle = new HashMap<>();
    }

    public int getShoppingAccount() {
        return shoppingAccount;
    }

    public double getShoppingTotalPrice() {
        return shoppingTotalPrice;
    }

    public Map<ComfoodDao, Integer> getShoppingSingleMap() {
        return shoppingSingle;
    }

    public boolean addShoppingSingle(ComfoodDao dish){
//        int remain = dish.getDishRemain();
//        if(remain<=0)
//            return false;
//        dish.setDishRemain(--remain);
        int num = 0;
        if(shoppingSingle.containsKey(dish)){
            num = shoppingSingle.get(dish);
        }
        num+=1;
        shoppingSingle.put(dish,num);
        Log.e("TAG", "addShoppingSingle: "+shoppingSingle.get(dish));

//        shoppingTotalPrice += dish.getPrice();
        shoppingTotalPrice = addDouble(shoppingTotalPrice,dish.getPrice());
        shoppingAccount++;
        return true;
    }

    public boolean subShoppingSingle(ComfoodDao dish){
        int num = 0;
        if(shoppingSingle.containsKey(dish)){
            num = shoppingSingle.get(dish);
        }
        if(num<=0) return false;
        num--;
//        int remain = dish.getDishRemain();
//        dish.setDishRemain(++remain);
        shoppingSingle.put(dish,num);
        if (num ==0) shoppingSingle.remove(dish);

        shoppingTotalPrice = subDouble(shoppingTotalPrice,dish.getPrice());
//        shoppingTotalPrice -= dish.getPrice();
        shoppingAccount--;
        return true;
    }

    public int getDishAccount() {
        return shoppingSingle.size();
    }

    public void clear(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle.clear();
    }

    /**
     * 加法运算
     * @param m1
     * @param m2
     * @return
     */
    public static double addDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.add(p2).doubleValue();
    }

    /**
     * 减法运算
     * @param m1
     * @param m2
     * @return
     */
    public static double subDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.subtract(p2).doubleValue();
    }
}
