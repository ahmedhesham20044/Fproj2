# 📚 Quantum Bookstore

A simple Java-based online bookstore system that supports multiple book types and allows inventory management, purchasing, and simulated delivery.

---

## ✅ Features

- **Add Books**: Supports `PaperBook`, `EBook`, and `DemoBook` types.
- **Buy Books**:
  - Paper books are shipped to a physical address.
  - EBooks are delivered via email (1 per purchase).
  - Demo books are display-only and cannot be purchased.
- **Inventory Management**:
  - Automatically removes outdated books (based on publication year).
  - Checks stock availability before purchase.
- **Extensible Design**: Easy to add new book types without changing the core logic.

---

## 📦 Book Types

| Type         | Stock | Purchasable | Delivery Method   |
|--------------|-------|-------------|--------------------|
| PaperBook    | ✅    | ✅          | ShippingService     |
| EBook        | ♾️    | ✅ (1 only) | MailService         |
| DemoBook     | ❌    | ❌          | Not Allowed         |

---

## 📂 Project Structure

```bash
.
├── QuantumBookstoreHumanized.java   # Main application code
└── README.md                        # This file
