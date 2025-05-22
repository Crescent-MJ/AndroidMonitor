[English](README.md) | 中文说明

---

# Android MITM 攻击检测应用

**一款用于检测 Android 平台中间人攻击并实时预警的应用**

作者：曹梦靖（Mengjing Cao）

---

## 项目概述

本项目是一款 Android 应用，旨在检测并提醒用户在使用公共 Wi-Fi 网络时可能遭受的中间人攻击（MITM）。该应用通过多种检测技术（如 ping 延迟分析、URL 错误检测、网络数据包捕获等）识别潜在威胁，并通过直观界面实时向用户发出警告。

---

## 项目动机

随着移动设备和公共 Wi-Fi 的广泛应用，用户越来越容易遭受网络安全风险，尤其是中间人攻击。现有的大多数安全工具要么价格昂贵，要么功能有限，普通用户和小型企业难以使用。本项目旨在提供一种低成本、实用的解决方案，提升大众的安全意识和防护能力。

---

## 主要功能

- **Wi-Fi 信息展示**：显示当前连接 Wi-Fi 的详细信息（IP、MAC、网关、速率等）。
- **网络数据包捕获与分析**：捕获并分析当前网络流量，检测异常活动。
- **Ping 延迟检测**：监控目标主机的 ping 延迟，识别通信被劫持或中断的迹象。
- **URL 错误检测**：结合域名解析和 URL 校验，识别可疑网站及钓鱼风险。
- **黑白名单机制**：内置可信与风险主机名单，自动比对并拦截高风险目标。
- **实时安全预警**：一旦检测到可疑行为，立即发出预警并指导用户应对。
- **用户友好界面**：设计简洁，便于理解和响应安全提示。

---

## 技术栈

- **平台**：Android
- **开发环境**：Android Studio
- **编程语言**：Java
- **主要模块**：
  - UI 设计（Wi-Fi 信息、数据包捕获、告警）
  - 网络流量拦截与分析
  - Ping/URL 检测算法
  - 本地数据库（日志与主机名单）

---

## 项目结构

```
.
├── .idea/                 # IDE 配置文件（可忽略）
├── app/                   # 主 Android 应用源码（Java/Kotlin、布局、资源）
├── gradle/wrapper/        # Gradle Wrapper 文件
├── monitor/               # （可选）额外模块或监控逻辑
├── .gitignore             # Git 忽略规则
├── README.md              # 项目英文文档
├── README-zh.md           # 项目中文文档（本文件）
├── build.gradle.kts       # 项目级 Gradle 构建脚本（Kotlin DSL）
├── gradle.properties      # Gradle 配置
├── gradlew                # Unix Gradle Wrapper
├── gradlew.bat            # Windows Gradle Wrapper
├── settings.gradle        # Gradle 设置（Groovy DSL）
├── settings.gradle.kts    # Gradle 设置（Kotlin DSL）
├── upload.gradle          # （可选）自定义 Gradle 上传脚本
```

### 目录说明

- **app/**  
  包含所有主要 Android 应用代码：
  - `src/`（Java/Kotlin 源码、Activity、Service 等）
  - `res/`（UI 布局、图片、字符串等资源）
  - `AndroidManifest.xml`（应用清单）

- **monitor/**  
  （如有）用于监控模块、服务或主应用之外的功能。

- **gradle/wrapper/**  
  Gradle Wrapper JAR 及配置，保证构建一致性。

- **.idea/**  
  IDE 专用配置（非必须）。

- **build.gradle.kts**、**settings.gradle(.kts)**  
  项目及模块构建配置文件。

---

### 使用方法

- 用 Android Studio 打开项目根目录。
- 主要开发在 `app/` 文件夹下进行。
- 多模块项目可在 `monitor/` 目录扩展功能。
- 配置与构建脚本位于根目录。

---

## 快速开始

1. 克隆本仓库到本地
2. 用 Android Studio 打开项目
3. 连接 Android 设备或启动模拟器
4. 构建并运行应用

---

## 个人贡献

- 独立完成需求分析、系统设计、编码实现与测试
- 设计并实现多维度 MITM 检测算法
- 完成全流程用户界面开发与优化
- 撰写完整项目文档与技术报告

---

## 致谢

感谢导师 Mehran Abolhasan 的指导与支持。

---

## 附录

- GitHub 上的代码帮助：https://github.com/lygttpod/AndroidMonitor
- Android 理论知识帮助：https://developer.android.com/

---
