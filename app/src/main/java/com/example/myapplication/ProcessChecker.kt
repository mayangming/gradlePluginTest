package com.example.myapplication

import android.content.Context
import java.io.FileReader

import java.io.BufferedReader

import java.io.IOException

import java.io.File
import android.content.pm.ResolveInfo

import android.content.pm.PackageManager

import android.content.Intent
import android.util.Log

import java.util.ArrayList

object ProcessChecker {
    /**
     * 获取当用户在试用的应用包名，适用于5.0以上
     *
     * @return
     */
    fun getForegroundApp(context: Context?): String? {
        val files = File("/proc").listFiles()
        var lowestOomScore = Int.MAX_VALUE
        var foregroundProcess: String? = null
        for (file in files) {
            if (!file.isDirectory) {
                continue
            }
            var pid: Int
            pid = try {
                file.name.toInt()
            } catch (e: NumberFormatException) {
                continue
            }
            try {
                val cgroup = read(String.format("/proc/%d/cgroup", pid))
                val lines = cgroup.split("\n").toTypedArray()
                var cpuSubsystem: String
                var cpuaccctSubsystem: String
                if (lines.size == 2) { // 有的手机里cgroup包含2行或者3行，我们取cpu和cpuacct两行数据
                    cpuSubsystem = lines[0]
                    cpuaccctSubsystem = lines[1]
                } else if (lines.size == 3) {
                    cpuSubsystem = lines[0]
                    cpuaccctSubsystem = lines[2]
                } else if (lines.size == 5) {
                    cpuSubsystem = lines[2]
                    cpuaccctSubsystem = lines[4]
                } else {
                    continue
                }
                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
                    continue
                }
                if (cpuSubsystem.endsWith("bg_non_interactive")) {
                    continue
                }
                val cmdline = read(String.format("/proc/%d/cmdline", pid))
//                if (isContainsFilter(cmdline)) {
//                    continue
//                }
                val uid = cpuaccctSubsystem.split(":").toTypedArray()[2].split("/")
                    .toTypedArray()[1].replace("uid_", "").toInt()
                if (uid >= 1000 && uid <= 1038) {
                    continue
                }
//                var appId: Int = uid - 1
//                while (appId > 1) {
//                    appId -= 1
//                }
//                if (appId < 0) {
//                    continue
//                }
                val oomScoreAdj = File(String.format("/proc/%d/oom_score_adj", pid))
                if (oomScoreAdj.canRead()) {
                    val oomAdj = read(oomScoreAdj.absolutePath).toInt()
                    if (oomAdj != 0) {
                        continue
                    }
                }
                val oomscore = read(String.format("/proc/%d/oom_score", pid)).toInt()
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore
                    foregroundProcess = cmdline
                }
                if (foregroundProcess == null) {
                    return null
                }
                val indexOf = foregroundProcess.indexOf(":")
                if (indexOf != -1) {
                    foregroundProcess = foregroundProcess.substring(0, indexOf)
                    Log.e("YM-->","--->应用名字:$foregroundProcess")
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return foregroundProcess
    }

    @Throws(IOException::class)
    private fun read(path: String): String {
        val output = StringBuilder()
        val reader = BufferedReader(FileReader(path))
        output.append(reader.readLine())
        var line = reader.readLine()
        while (line != null) {
            output.append('\n').append(line)
            line = reader.readLine()
        }
        reader.close()
        return output.toString().trim { it <= ' ' } // 不调用trim()，包名后会带有乱码
    }

    /**
     * 是否为桌面应用
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isLauncherApp(context: Context, packageName: String): Boolean {
        val names: MutableList<String> = ArrayList()
        val packageManager: PackageManager = context.getPackageManager()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val list =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) //
        for (resolveInfo in list) {
            names.add(resolveInfo.activityInfo.packageName)
        }
        return if (names.contains(packageName)) {
            true
        } else false
    }

    /**
     * 判断是否为第三方应用，并且有界面的应用
     *
     * @param context
     * @param packageName
     * @return true:第三方应用，并且有界面
     */
    fun isUserApp(context: Context, packageName: String): Boolean {
        val names: MutableList<String> = ArrayList()
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val list =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) //
        for (resolveInfo in list) {
            names.add(resolveInfo.activityInfo.packageName)
        }
        if (!names.contains(packageName)) {
            if (packageManager.getLaunchIntentForPackage(packageName) != null) {
                return true
            }
        }
        return false
    }

    /**
     * filter包名过滤
     *
     * @param cmdline
     * @return
     */
//    fun isContainsFilter(cmdline: String): Boolean {
//        var flag = false
//        if (filterMap == null || filterMap.isEmpty() || filterMap.size() === 0) {
//            initFliter()
//        }
//        if (filterMap != null) {
//            for (key in filterMap.keySet()) {
//                if (cmdline.contains(key!!)) {
//                    flag = true
//                    break
//                }
//            }
//        }
//        return flag
//    }
}