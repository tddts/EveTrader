DELETE FROM CACHED_ORDER WHERE ORDER_PK IN (
    SELECT MIN(ORDER_PK) AS PK FROM CACHED_ORDER
        WHERE ORDER_ID IN (
            SELECT ORDER_ID FROM (
                SELECT ORDER_ID, COUNT(ORDER_ID) AS CNT FROM CACHED_ORDER GROUP BY ORDER_ID)
                WHERE CNT > 1)
    GROUP BY ORDER_ID);