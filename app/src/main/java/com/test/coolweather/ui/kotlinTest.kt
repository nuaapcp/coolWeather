package com.test.coolweather.ui


import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    getSms().forEach {
        praseSms(it)
    }
//    testTime()
}

/**
 * 依据始末标志（"0a","00"）将短信总内容提取出来
 *@return 包含所有短信总内容（总内容=短信内容+号码、时间及其他标志位等）的list
 */
fun getSms(): List<ByteArray> {
    val result: MutableList<ByteArray> = emptyList<ByteArray>().toMutableList()
    var smsCount = 0;
    val file = File("C:\\Users\\lenovo\\Desktop\\_tmp_bak_real")
    val fin = BufferedInputStream(file.inputStream())
    val b = ByteArray(1)
    try {
        val estimateFileSize = fin.available()
        while (fin.read(b, 0, 1) != -1) {
            val toHexString = Integer.toHexString(b[0].toInt())
            if (toHexString.toLowerCase(Locale.ROOT) == "a") {
                fin.read(b, 0, 1)
                var size = byte2UnsignedInt(b[0])
                //长度大于128字节时，以两个字节表示长度
                if (size > 128) {
                    val temp = ByteArray(1)
                    fin.read(temp, 0, 1)
                    size += (byte2UnsignedInt(temp[0]) - 1) * 128
                }
                if (size in 1 until estimateFileSize) {
                    fin.mark(estimateFileSize)
                    fin.skip(size.toLong() - 1)
                    val temp = ByteArray(1)
                    val readTemp = fin.read(temp, 0, 1)
                    if (Integer.toHexString(temp[0].toInt()) == "0" && readTemp != -1) {
                        //是短信内容
                        fin.reset()
                        val mContent = ByteArray(size)
                        fin.read(mContent)
                        result.add(mContent)
                        smsCount++
                    } else {
                        fin.reset()
                    }
                }
            }
        }
    } catch (e: IOException) {
        print(e.message)
    } finally {
        fin.close()
    }
    print("getSms: 共${smsCount}条短信\n")
    return result
}


fun byte2UnsignedInt(byte: Byte): Int {
    return if (byte < 0x0) (byte.toUByte()).toInt() else byte.toInt()
}

fun dateTest(date: Long) {
    val d = Date(date)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA)
    val dateString = dateFormat.format(d).toString()
    print(dateString)
}

/**
 * 解析一条短信，读取其中的内容、号码、状态、时间等内容
 */
fun praseSms(mcontent: ByteArray) {
    var type: Int? = null
    var number: String? = null
    var message: String? = null
    var date: String? = null
    var i = 0
    while (i < mcontent.size) {
        val toHexString = Integer.toHexString(mcontent[i].toInt())
        if (toHexString == "18" && type == null) {
            type = mcontent[i + 1].toInt()
            i++
        } else if (toHexString == "22" && number == null && type != null) {
            val numberLength = mcontent[i + 1].toInt()
            number = String(mcontent).substring(i + 2, i + 2 + numberLength)
            i += 1 + numberLength
        } else if (toHexString == "32" && message == null && type != null && number != null) {
            var messageLength = byte2UnsignedInt(mcontent[i + 1])
            //短信真正内容的起始位置
            var messageOffset = i + 2
            //长度大于128字节时，以两个字节表示长度
            if (messageLength > 128) {
                messageLength += (byte2UnsignedInt(mcontent[i + 2]) - 1) * 128
                messageOffset += 1
            }
            val tempByteArray = ByteArray(messageLength)
            try {
                mcontent.copyInto(tempByteArray, 0, messageOffset,
                        messageOffset + messageLength)
            } catch (e: IndexOutOfBoundsException) {
                print(e.message)
            }
            message = String(tempByteArray)
            i += 1 + messageLength
        } else if (toHexString == "38" && date == null && type != null && number != null
                && message != null) {
            val tempByteArray = ByteArray(8)
            try {
                mcontent.copyInto(tempByteArray, 0, i + 1,
                        i + 1 + 8)
            } catch (e: IndexOutOfBoundsException) {
                print(e.message)
            }
            date = String(tempByteArray)
            i += 8
        } else {
            i++
        }
    }
    print("type======$type\n")
    print("number======$number\n")
    print("message======$message\n")
    print("date======$date\n")
}

fun testTime() {
    val high: Long = 0x33a9a1e6
    val low: Long = 0x01c404cb
    val value = high.shl(32).and(0xffffffff).or(low.and(0xffffffff))
    val MILLISECOND_MULTIPLE = 10000
    val UNIX_FILETIME_DIFF = 11644473600000L
    val final = value / MILLISECOND_MULTIPLE - UNIX_FILETIME_DIFF
    dateTest(final)
}