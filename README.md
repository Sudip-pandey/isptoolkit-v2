# ISP TOOLKIT — Network, Wi-Fi & Fiber Diagnostics

**Brand:** BUILT BY PANDEY  
**Package:** `com.pandey.isptoolkit`  
**Target Platform:** Native Android (API level 26 to 35)  
**Architecture:** Clean Architecture + MVVM + Jetpack Compose + Material 3 + Hilt + Room + DataStore

---

## 🛠 Product Concept

**ISP TOOLKIT** is a professional, production-grade field diagnostics toolbox designed specifically for Internet Service Provider (ISP) network engineers and fiber field technicians.

Unlike generic Wi-Fi analyzers, **ISP TOOLKIT** prioritizes **100% real data**, mathematical calculation accuracy, evidence-based customer complaint troubleshooting, and complete offline capability.

> [!IMPORTANT]
> **No Fake Data Policy:** If Android APIs restrict or do not expose specific diagnostic metrics (e.g. MAC address on Android 10+, unexposed hardware details), the application explicitly displays **Unavailable**, **Unknown**, or **Permission Required**. The app *never* invents fake values.

---

## 🔬 Core Features & Toolset

### 1. Fiber / Optical Toolkit
- **Optical Power Calculator:** Exact bidirectional conversion between $\text{dBm}$ and $\text{mW} / \mu\text{W}$ without internal rounding:
  $$\text{mW} = 10^{\frac{\text{dBm}}{10}}, \quad \text{dBm} = 10 \times \log_{10}(\text{mW})$$
- **PLC Splitter Calculator:** Support for ratios ($1:2, 1:4, 1:8, 1:16, 1:32, 1:64$) and custom splits with Ideal Theoretical vs Practical Estimated loss modeling (Insertion, Excess, Connector, Splice losses).
- **Asymmetric & Symmetric Coupler Calculator:** Independent Port A / Port B calculations ($50:50, 45:55, 30:70, 20:80, 10:90, 5:95$, custom %).
- **Cascaded Splitter Chain Builder:** Build dynamic multi-stage optical chains ($\text{OLT} \to \text{PLC} \to \text{Coupler} \to \text{ONT}$) with stage reordering, stage-by-stage power drop calculations, and history saving.
- **Link Budget Calculator:** Wavelength-specific attenuation ($1310\text{ nm}, 1490\text{ nm}, 1550\text{ nm}, 1625\text{ nm}$), connector/splice loss counts, receiver sensitivity & overload risk bounds ($\text{PASS}, \text{MARGIN LOW}, \text{BELOW SENSITIVITY}, \text{OVERLOAD RISK}$).
- **Fiber Loss & Connector/Splice Loss Calculators.**
- **ONT Optical Reading History:** Record PON states ($\text{O1-O5}$), LOS alarms, RX/TX power readings, serial numbers, OLT slots, and technician notes.

### 2. Network & Wireless Diagnostics
- **Real Android Network APIs:** Active interface status, IPv4, IPv6, Gateway default route, DNS servers, interface names, and metered state.
- **Wi-Fi Analyzer & AP Distribution:** Access point discovery, BSSID/SSID filtering, $2.4\text{ GHz}, 5\text{ GHz}, 6\text{ GHz}$ channel distribution.
- **Live Signal Meter:** Rolling RSSI canvas graph with real-time minimum, maximum, average sampling and battery-optimized sampling intervals.
- **LAN Device Discovery:** Bounded-concurrency ICMP sweep and mDNS/NSD host discovery with confidence rating ($\text{HIGH}, \text{MEDIUM}, \text{LOW}$) and evidence categorization.

### 3. Technician Dashboard & Customer Complaint Workflows
- **Dashboard Overview:** Ping latency, measured download/upload speed, RSSI, and active connection status.
- **Guided Complaint Troubleshooting:** Workflow engines for *Internet Slow*, *No Internet*, *Weak Wi-Fi*, *High Ping*, and *ONT Red LOS*. Outputs probable root causes with measured evidence logs.

### 4. IP, Subnet & Field Engineering Tools
- **Subnet Calculator:** Comprehensive IPv4 CIDR $/0$ to $/32$ calculation (supporting RFC 3021 $/31$ point-to-point links and $/32$ host routes), netmask, wildcard mask, network address, broadcast address, and host bounds.
- **IPv6 Calculator:** Prefix length and address type classification ($\text{Global Unicast}, \text{Link-Local}, \text{Unique-Local}, \text{Loopback}, \text{Multicast}$).
- **PPPoE, VLAN & MTU Tools:** Overhead payload calculations, VLAN ID validation ($1 - 4094$), PCP 802.1p priority references.

### 5. Site Management & Before/After Visits
- Local Room Database storage for site profiles, visit logs, saved calculations, and before/after visit comparison diffs (RSSI, Latency, Loss, ONT RX/TX).

---

## 🔒 Privacy & Authorized Use

- **Local Storage Default:** All saved calculations, ONT readings, site profiles, and scanned LAN device records remain 100% on the local device within Room DB.
- **Zero Credential Collection:** ISP TOOLKIT *never* requests, prompts for, or stores Wi-Fi passwords, PPPoE passwords, or router admin credentials.
- **Authorized Use Statement:** This application is intended solely for authorized field diagnostics, network maintenance, and subscriber troubleshooting by field technicians.

---

## ⚙️ Building the Application

### Prerequisites
- JDK 17 or higher
- Android SDK (API 34/35)

### Build Commands

```bash
# Make gradlew executable
chmod +x gradlew

# Run all unit tests
./gradlew testDebugUnitTest

# Assemble Debug APK
./gradlew assembleDebug
```

---

## 🚀 GitHub Actions CI/CD

An automated workflow is configured at `.github/workflows/android.yml` to automatically run unit tests and compile the debug APK on every commit and pull request.

---

**Built by PANDEY** — Native Android ISP Diagnostic Engineering.
