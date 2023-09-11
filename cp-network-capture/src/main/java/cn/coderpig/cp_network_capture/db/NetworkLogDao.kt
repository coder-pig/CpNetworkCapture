package cn.coderpig.cp_network_capture.db

import android.content.ContentValues
import android.net.Uri
import cn.coderpig.cp_network_capture.entity.NetworkLog
import cn.coderpig.cp_network_capture.provider.CpNetworkCaptureProvider

/**
 * Author: zpj
 * Date: 2023-09-05 14:14
 * Desc:
 */
class NetworkLogDao(private val db: NetworkLogDB) {
    companion object {
        const val TABLE_NAME = "network_log"

        /**
         * 建表SQL语句
         * */
        fun createTableSql() = StringBuilder("CREATE TABLE $TABLE_NAME(").apply {
            append("id INTEGER PRIMARY KEY AUTOINCREMENT,")
            append("method TEXT,")
            append("url TEXT,")
            append("scheme TEXT,")
            append("protocol TEXT,")
            append("host TEXT,")
            append("path TEXT,")
            append("duration INTEGER,")
            append("requestTime INTEGER,")
            append("requestHeaders TEXT,")
            append("requestBody TEXT,")
            append("requestContentType TEXT,")
            append("responseCode INTEGER,")
            append("responseTime INTEGER,")
            append("responseHeaders TEXT,")
            append("responseBody TEXT,")
            append("responseMessage TEXT,")
            append("responseContentType TEXT,")
            append("responseContentLength INTEGER,")
            append("errorMsg STRING,")
            append("source STRING")
            append(")")
        }.toString()
    }


    /**
     * 插入数据
     * */
    fun insert(data: NetworkLog) {
        db.writableDatabase.insert(TABLE_NAME, null, ContentValues().apply {
            put("method", data.method)
            put("url", data.url)
            put("scheme", data.scheme)
            put("protocol", data.protocol)
            put("host", data.host)
            put("path", data.path)
            put("duration", data.duration)
            put("requestTime", data.requestTime)
            put("requestHeaders", data.requestHeaders)
            put("requestBody", data.requestBody)
            put("requestBody", data.requestBody)
            put("requestContentType", data.requestContentType)
            put("responseCode", data.responseCode)
            put("responseTime", data.responseTime)
            put("responseHeaders", data.responseHeaders)
            put("responseBody", data.responseBody)
            put("responseMessage", data.responseMessage)
            put("responseContentType", data.responseContentType)
            put("responseContentLength", data.responseContentLength)
            put("errorMsg", data.errorMsg)
            put("source", data.source)
        })
        NetworkCapture.context?.contentResolver?.notifyChange(NetworkCapture.networkLogTableUri, null)
    }

    /**
     * 查询数据
     * @param offset 第几页，从0开始
     * @param limit 分页条数
     * */
    fun query(
        offset: Int = 0,
        limit: Int = 20,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): ArrayList<NetworkLog> {
        val logList = arrayListOf<NetworkLog>()
        val cursor = db.readableDatabase.query(
            TABLE_NAME, null, selection, selectionArgs, null, null, "id DESC", "${offset * limit},${limit}"
        )
        if (cursor.moveToFirst()) {
            do {
                logList.add(NetworkLog().apply {
                    id = cursor.getLong(0)
                    method = cursor.getString(1)
                    url = cursor.getString(2)
                    scheme = cursor.getString(3)
                    protocol = cursor.getString(4)
                    host = cursor.getString(5)
                    path = cursor.getString(6)
                    duration = cursor.getLong(7)
                    requestTime = cursor.getLong(8)
                    requestHeaders = cursor.getString(9)
                    requestBody = cursor.getString(10)
                    requestContentType = cursor.getString(11)
                    responseCode = cursor.getInt(12)
                    responseTime = cursor.getLong(13)
                    responseHeaders = cursor.getString(14)
                    responseBody = cursor.getString(15)
                    responseMessage = cursor.getString(16)
                    responseContentType = cursor.getString(17)
                    responseContentLength = cursor.getLong(18)
                    errorMsg = cursor.getString(19)
                    source = cursor.getString(20)

                })
            } while (cursor.moveToNext())
        }
        cursor.close()
        return logList
    }

    /**
     * 根据id删除数据
     * @param id 记录id
     * */
    fun deleteById(id: Long) {
        db.writableDatabase.delete(TABLE_NAME, "id = ?", arrayOf("$id"))
    }

    /**
     * 清空数据
     * */
    fun clear() {
        db.writableDatabase.delete(TABLE_NAME, null, null)
    }
}