/**
 * SO SÁNH HÀNH VI KHI SERVER DỪNG GIỮA LÚC CLIENT ĐANG HOẠT ĐỘNG
 * ================================================================
 *
 * 1. TCP (Transmission Control Protocol):
 *    - TCP là giao thức hướng kết nối (connection-oriented).
 *    - Khi server dừng đột ngột, kết nối TCP bị đóng.
 *    - Client sẽ nhận được IOException (hoặc readLine() trả về null)
 *      ngay khi cố gửi hoặc đọc dữ liệu tiếp theo.
 *    - Client BIẾT NGAY server đã ngắt kết nối → có thể xử lý lỗi.
 *    - Dữ liệu đã gửi trước khi server dừng được đảm bảo đến đích
 *      (nếu server kịp nhận trước khi tắt).
 *
 * 2. UDP (User Datagram Protocol):
 *    - UDP là giao thức không kết nối (connectionless).
 *    - Khi server dừng, client KHÔNG BIẾT vì không có kết nối nào bị đóng.
 *    - Client vẫn gửi packet bình thường nhưng không nhận được phản hồi.
 *    - Client bị treo ở socket.receive() cho đến khi hết timeout.
 *    - Nếu không đặt setSoTimeout(), client sẽ bị treo vĩnh viễn.
 *    - Không có cơ chế tự động thông báo server đã dừng.
 *
 * 3. Tóm tắt:
 *    +------------------+----------------------------+----------------------------+
 *    | Tiêu chí         | TCP                        | UDP                        |
 *    +------------------+----------------------------+----------------------------+
 *    | Phát hiện lỗi    | Ngay lập tức (IOException) | Phải chờ timeout           |
 *    | Dữ liệu mất      | Không (đã ACK)             | Có thể mất không biết     |
 *    | Client bị treo   | Không                      | Có (nếu không có timeout) |
 *    | Xử lý phía client| Bắt exception dễ dàng      | Cần setSoTimeout() thủ công|
 *    +------------------+----------------------------+----------------------------+
 *
 * Kết luận:
 *    TCP đáng tin cậy hơn trong việc phát hiện server ngắt kết nối.
 *    UDP cần cơ chế timeout và retry do client tự quản lý.
 */
