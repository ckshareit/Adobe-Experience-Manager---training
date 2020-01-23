"use strict";

use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function (AuthoringUtils) {
    var CONST = {
        PROP_SYMBOL: "symbol",
        PROP_SUMMARY: "summary",
    }

    var SHOW_PROP = {
        REQUESTDATE: "showRequestDate",
        REQUESTTIME: "showRequestTime",
        UPDOWN: "showUpDown",
        OPENPRICE: "showOpenPrice",
        RANGEHIGH: "showRangeHigh",
        RANGELOW: "showRangeLow",
        VOLUME: "showVolume",
    }
    var TITLE = {
        CURRENTPRICE: "Current Price",
        REQUESTDATE: "Request Date",
        REQUESTTIME: "Request Time",
        UPDOWN: "Up/Down",
        OPENPRICE: "Open Price",
        RANGEHIGH: "Range High",
        RANGELOW: "Range Low",
        VOLUME: "Volume"
    }

    var stockplex = {};
    stockplex.items = []; //each item holds the [Name, Value] to be displayed on the page

    stockplex.symbol = properties.get(CONST.PROP_SYMBOL, "");
    stockplex.summary = properties.get(CONST.PROP_SUMMARY, "");

    //If no symbol or summary has been given, show a touch ui placeholder
    if (stockplex.symbol == "" && stockplex.summary == "") {
        if (AuthoringUtils.isTouch) {
            //Set placeholder class for touch
            stockplex.cssClass = "cq-placeholder";
        } else {
            //Set placeholder class for classic
            stockplex.cssClass = "cq-text-placeholder-ipe";
        }
        return stockplex;
    }

    //Gets the Stock information from the StockDataImporter class
    var stock;
    var res = resource.getResourceResolver().getResource("/content/" + stockplex.symbol);
    if (res == null) {
        stockplex.currentPrice = "No Stock importer created for this stock."
    } else {
        stock = res.adaptTo(com.adobe.training.core.models.StockModel);
        //Get the current stock
        stockplex.currentPrice = stock.getLastTrade();
    }

    //Add the appropriate properties to the item array to be displayed
    if (properties.get(SHOW_PROP.REQUESTDATE)) {
        stockplex.items.push([
            TITLE.REQUESTDATE,
            (stock) ? stock.getRequestDate() : "11/13/1988"
            ]);
    }
    if (properties.get(SHOW_PROP.REQUESTTIME)) {
        stockplex.items.push([
            TITLE.REQUESTTIME, 
            (stock) ? stock.getRequestTime() : '10:00AM'
        ]);
    }
    if (properties.get(SHOW_PROP.UPDOWN)) {
        stockplex.items.push([
            TITLE.UPDOWN, 
            (stock) ? stock.getUpDown() : 2
        ]);
    }
    if (properties.get(SHOW_PROP.OPENPRICE)) {
        stockplex.items.push([
            TITLE.OPENPRICE, 
            (stock) ? stock.getOpenPrice() : 13
        ]);
    }
    if (properties.get(SHOW_PROP.RANGEHIGH)) {
        stockplex.items.push([
            TITLE.RANGEHIGH, 
            (stock) ? stock.getRangeHigh() : 27
        ]);
    }
    if (properties.get(SHOW_PROP.RANGELOW)) {
        stockplex.items.push([
            TITLE.RANGELOW, 
            (stock) ? stock.getRangeLow() : 18
        ]);
    }
    if (properties.get(SHOW_PROP.VOLUME)) {
        stockplex.items.push([
            TITLE.VOLUME, 
            (stock) ? stock.getVolume() : 35911
        ]);
    }

    //Get the download button boolean from the Designer
    stockplex.downloadButton = currentStyle.get("downloadButton", false);

    return stockplex;

});