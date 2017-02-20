INSERT INTO TMP_SELL_ORDERS (ORDER_PK, ORDER_ID, LOCATION_ID, PRICE, VOLUME)
SELECT ORDER_PK, ORDER_ID, LOCATION_ID, PRICE, VOLUME_REMAIN
	FROM ORDER_SEARCH_CACHE
	INNER JOIN TMP_STATIONS
	ON TMP_STATIONS.STATION_ID = ORDER_SEARCH_CACHE.LOCATION_ID
	WHERE ORDER_SEARCH_CACHE.TYPE_ID = :type_param
	AND ORDER_SEARCH_CACHE.BUY_ORDER = FALSE;