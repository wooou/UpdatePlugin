/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lzh.framework.updatepluginlib.strategy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

import org.lzh.framework.updatepluginlib.model.UpdateInterface;
import org.lzh.framework.updatepluginlib.util.ActivityManager;
import org.lzh.framework.updatepluginlib.util.UpdateInstallProvider;
import org.lzh.framework.updatepluginlib.util.Utils;

import java.io.File;

/**
 * 默认的安装策略实现类. 适配Android 7.0安装方案。
 *
 * @author haoge
 */
public class DefaultInstallStrategy implements InstallStrategy{

    private static final int REQUEST_INSTALL = 0x010101;
    private static String DEFAULT_AUTHOR = null;

    @Override
    public void install(Context context, String filename, UpdateInterface update) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        File pluginFile = new File(filename);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            // Adaptive with api version 24+
            uri = UpdateInstallProvider.getUriByFile(pluginFile,getAuthor(context));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(pluginFile);
        }
        intent.setDataAndType(uri, type);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent,REQUEST_INSTALL);
        }
        context.startActivity(intent);

        exitIfForceUpdate(update);
    }

    /**
     * 进行判断是否是强制更新：若是则强制退出应用。
     * @param update 更新实体数据
     */
    protected void exitIfForceUpdate(UpdateInterface update) {
        if (!update.isForced()) {
            return;
        }
        // 延迟强制更新时的退出操作。因为部分机型上安装程序读取安装包的时机有延迟。
        Utils.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 释放Activity资源。避免进程被杀后导致自动重启
                ActivityManager.get().finishAll();
                // 两种kill进程方式一起使用。双管齐下！
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }, 1500);

    }

    private String getAuthor(Context context) {
        if (TextUtils.isEmpty(DEFAULT_AUTHOR)) {
            DEFAULT_AUTHOR = "update.plugin." + context.getPackageName() + ".UpdateInstallProvider";
        }
        return DEFAULT_AUTHOR;
    }
}
