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
package org.lzh.framework.updatepluginlib.model;

import org.lzh.framework.updatepluginlib.creator.DefaultNeedInstallCreator;
import org.lzh.framework.updatepluginlib.creator.DefaultNeedUpdateCreator;
import org.lzh.framework.updatepluginlib.strategy.ForcedUpdateStrategy;

/**
 * 此实体类用于存储框架所需的更新数据。
 *
 * @author haoge
 */
public interface UpdateInterface {

    /**
     * <p>指定是否要求进行强制更新。当设置为强制更新时，将会导致设置的更新策略无效。
     * 而直接使用框架内部所提供的{@link ForcedUpdateStrategy}进行更新策略管理
     *
     * @return True代表此版本需要进行强制更新
     */
     boolean isForced();


    /**
     * <p>指定是否要求展示忽略此版本更新按钮：
     *
     * <p>此属性为非必须属性。即框架核心操作层并未依赖此属性。此属性只用于提供的默认弹窗通知中：{@link DefaultNeedInstallCreator} 和 {@link DefaultNeedUpdateCreator}
     *
     * @return True代表在弹出通知中进行展示 <b>忽略此版本更新按钮</b>
     */
     boolean isIgnore();
    /**
     * 设置此次版本更新内容，将在更新弹窗通知中使用
     * @return  更新内容
     */
     String getUpdateContent();
    /**
     * 设置此次版本的远程更新apk包
     * @return  更新包url地址
     */
     String getUpdateUrl();
    /**
     * 新版本apk的版本号。此版本号将被用于与本地apk进行版本号比对。判断该版本是否应该被更新. 默认版本号检查器为：{@link DefaultChecker}.
     * @return  apk版本号
     */
     int getVersionCode();
    /**
     * 新版本的apk的版本名。
     * @return  version name
     */
     String getVersionName();

}
