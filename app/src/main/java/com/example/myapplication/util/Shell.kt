package com.example.myapplication.util

import android.util.Log
import java.io.*

import java.io.InputStreamReader
import java.nio.charset.Charset
import java.io.BufferedReader
import java.io.IOException

import java.util.Arrays

import java.io.InputStream








object Shell {

    fun sendCmd(cmd: String){
        var p: ProcessBuilder ?= null
        var x:Process ?=null;
        try {
            p = ProcessBuilder("/system/bin/sh", "-c",cmd);
            var env = p.environment();
//            for( variable in envirounmentVariables.entrySet()){
//                env.put(variable.getKey(), variable.getValue());
//            }
            p.redirectErrorStream(true);

            x = p.start();

            try {
                x.waitFor();
            } catch ( e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch ( e: IOException) {
            e.printStackTrace();
        }
        try {
            toStings(x?.inputStream)
        } catch ( e : IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private fun toStings(inputStream: InputStream?){
        val stringBuilder = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var firstLine = true
        var line: String? = null
        while (bufferedReader.readLine().also { line = it } != null) {
            if (!firstLine) {
                stringBuilder.append(System.getProperty("line.separator"))
            } else {
                firstLine = false
            }
            stringBuilder.append(line)
        }
        Log.e("YM","result:${stringBuilder.toString()}")
    }
}