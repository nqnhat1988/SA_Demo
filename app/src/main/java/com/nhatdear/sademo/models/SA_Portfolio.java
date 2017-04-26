package com.nhatdear.sademo.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nhatdear on 4/25/17.
 */

public class SA_Portfolio {
    private String portfolioId;
    private ArrayList<SA_Nav> navs;

    public SA_Portfolio(HashMap porfolioSnapshot) {
        this.setPortfolioId(porfolioSnapshot.get("portfolioId").toString());

        ArrayList<HashMap> navHashMap = (ArrayList<HashMap>) porfolioSnapshot.get("navs");
        ArrayList<SA_Nav> navs = new ArrayList<SA_Nav>();
        try {
            for (HashMap hashMap : navHashMap) {
                SA_Nav nav = new SA_Nav();
                if (hashMap.containsKey("amount") == false)
                    nav.setAmount(0.0);
                else
                    nav.setAmount(Double.parseDouble(hashMap.get("amount").toString()));
                nav.setDate(hashMap.get("date").toString());
                navs.add(nav);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setNavs(navs);
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public ArrayList<SA_Nav> getNavs() {
        return navs;
    }

    public void setNavs(ArrayList<SA_Nav> navs) {
        this.navs = navs;
    }
}
