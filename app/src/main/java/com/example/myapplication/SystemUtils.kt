package com.example.myapplication

import android.content.Context
import android.text.TextUtils
import android.util.Log
import java.io.File

//系统工具类
object SystemUtils {
    val TAG = "YM--->SystemUtils"
        //Android11版本时候该函数废弃
        fun getAllPackage(context: Context): List<String>{
            val packageManager = context.packageManager
            val packageInfos = packageManager.getInstalledPackages(0)
            val packageNames = mutableListOf<String>()
            packageInfos.forEach {
                packageNames.add(it.packageName)
            }
            return packageNames
        }

    /**
     * 获取缓存文件夹大小
     * 主要获取内置/外置内存卡 cache文件夹下面所有的文件和文件夹
     */
    fun getCacheDirSize(context: Context): Long {
        return try {
            getFolderSize(context.externalCacheDir!!) + getFolderSize(context.cacheDir)
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * 获取缓存文件大小
     * @param filePath 文件路径
     */
    fun getFolderSize(filePath: String?): Long {
        return if (TextUtils.isEmpty(filePath)) {
            0L
        } else getFolderSize(File(filePath))
    }

    /**
     * 获取缓存文件大小
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     *
     * @param file 具体文件
     */
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size = if (fileList[i].isDirectory) {
                    size + getFolderSize(fileList[i])
                } else {
                    size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(TAG, "getFolderSize() called with: file = [$file]--size = [$size]")
        return size
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath 是否删除传入的文件
     * @param file       文件
     */
    fun deleteFolderFile(file: File?, deleteThisPath: Boolean) {
        if (file != null) {
            try {
                if (file.isDirectory) { // 如果下面还有文件
                    val files = file.listFiles()
                    for (i in files.indices) {
                        deleteFolderFile(files[i].absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) { // 如果是文件，删除
                        file.delete()
                    } else { // 目录
                        if (file.listFiles().size == 0) { // 目录下没有文件或者目录，删除
                            file.delete()
                        }
                    }
                }
                Log.d(
                    TAG,
                    "deleteFolderFile() called with: file = [$file], deleteThisPath = [$deleteThisPath]"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath 是否删除传入的文件
     * @param filePath       文件路径
     */
    fun deleteFolderFile(filePath: String?, deleteThisPath: Boolean) {
        if (!TextUtils.isEmpty(filePath)) {
            deleteFolderFile(File(filePath), deleteThisPath)
        }
    }
}