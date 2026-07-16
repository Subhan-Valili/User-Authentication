# User Authentication & Authorization System

Bu layihə, **Spring Boot 3.x** və **Spring Security** əsasında qurulmuş, yüksək təhlükəsizlik standartlarına cavab verən **Authentication (Autentifikasiya)** və **Authorization (Avtorizasiya)** sistemidir. Layihədə istifadəçi sessiyalarının təhlükəsiz idarə edilməsi üçün **JWT (JSON Web Token)** və **Redis** inteqrasiyalı **Refresh Token Rotation** mexanizmi tətbiq olunmuşdur.

---

## 🚀 Xüsusiyyətlər (Features)

* **İkiqat Verilənlər Bazası Arxitekturası:** İnteqrasiya olunmuş JPA (RDBMS) və Redis verilənlər bazası.
* **JWT Authentication:** `@Component` əsaslı təhlükəsiz Access Token istehsalı və doğrulanması.
* **Redis ilə Refresh Token İdarəedilməsi:** Refresh tokenlər Redis bazasında **TTL (Time-To-Live)** parametri ilə saxlanılır. Token yenilənərkən köhnə token silinir və yenisi yaradılır (Token Rotation).
* **Role-Based Access Control (RBAC):** `@PreAuthorize` annotasiyası vasitəsilə metod səviyyəsində rol (`ROLE_USER`, `ROLE_ADMIN`) və icazə (`READ`) yoxlanışları.
* **Qlobal Xəta İdarəetməsi (Global Exception Handling):** Xüsusi istisnalar (`UserAlreadyExists`, `RoleNotFound`, `NotFound`) üçün mərkəzləşdirilmiş idarəetmə.

---

## 🛠 Texnologiya Steki (Tech Stack)

* **Backend Framework:** Spring Boot 3.x
* **Security:** Spring Security, JWT (Json Web Token)
* **Database (JPA):** PostgreSQL / MySQL (Hibernate)
* **Caching & Session:** Redis (Spring Data Redis)
* **Lombok:** Kodun oxunabilirliyini artırmaq üçün boilerplate kodların azaldılması.

---

## 📁 API Endpoint-ləri

### 🔐 Autentifikasiya Controller (`/api/v1/auth`)

| Metod | Endpoint | Açıqlama | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Yeni standart istifadəçi qeydiyyatı | Public |
| `POST` | `/register-admin` | Yeni admin qeydiyyatı | Public |
| `POST` | `/login` | İstifadəçi girişi (Access Token & Refresh Token ID qaytarır) | Public |
| `POST` | `/refresh` | Vaxtı bitmiş Access Token-i Refresh Token ilə yeniləyir | Public |

### 🛡 İcazələr və Giriş Controller (`/api/v1/permission`)

| Metod | Endpoint | Tələb Olunan Rol / İcazə |
| :--- | :--- | :--- |
| `GET` | `/admin` | `ROLE_ADMIN` |
| `GET` | `/user` | `ROLE_USER` və ya `ROLE_ADMIN` |
| `GET` | `/read` | `hasAuthority('READ')` AND (`ROLE_USER` və ya `ROLE_ADMIN`) |

---

## 💡 Refresh Token Mexanizmi Necə İşləyir?

1. İstifadəçi `/login` sorğusu göndərir.
2. Sistem istifadəçini yoxlayır, **Access Token (JWT)** istehsal edir və Redis bazasında unikal bir `UUID` ilə **Refresh Token** yaradır.
3. Access tokenin vaxtı bitdikdə, müştəri (client) `/refresh` endpoint-inə Redis-dəki token ID-ni göndərir.
4. Sistem köhnə Refresh Token-i silir, yeni bir dənəsini istehsal edir və yeni Access Token ilə birlikdə geri qaytarır. Bu metod təhlükəsizliyi maksimuma çatdırır.

---

## 🛠 Layihəni Lokal Mühitdə İşə Salmaq (Setup)

### Tələblər
* Java 17 və ya daha yuxarı versiya
* Gradle
* Docker (Redis üçün)

### Addımlar

1. **Layihəni klonlayın:**
   ```bash
   git clone [https://github.com/your-username/user-authentication-system.git](https://github.com/your-username/user-authentication-system.git)
   cd user-authentication-system
