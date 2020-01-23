"use strict";

use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function (AuthoringUtils) {
    
	var symbols = this.symbol;
    var failures = "";

    if(!symbols || symbols.length < 1)
    	return " NO_SYMBOLS_TO_TEST ";
    
    var i;
    for (i = 0; i < symbols.length; i++) {
    	var curSymbol = symbols[i];
        var res = resource.getResourceResolver().getResource("/content/" + curSymbol);
		log.info("Testing: " + curSymbol);
        //Check if Stock Symbol exists
        if (res != null) {
            var stockModel = res.adaptTo(com.adobe.training.core.models.StockModel);

            //if there is no last trade or the last trade is below 0, add to failures
            if((stockModel == null) 
               || (stockModel.getLastTrade() < 0)){
            	failures = failures + " " + curSymbol;
            }
        }
        else {
            failures = failures + " " + curSymbol;
        }
    }
    
    //If there are any symbols that fail, return them
    if(failures)
    	return " FAILED_SYMBOLS: [" + failures.trim().replace(/ /g,",") + "] ";
    else
    	return "TEST_PASSED"; //Keyword to allow the test to pass
});