
## 1. Bắt buộc đọc trước khi làm việc

Trước khi phân tích, sửa code, thêm dependency, đổi cấu trúc hoặc chạy lệnh có thể thay đổi dữ liệu, agent MUST:

1. Đọc toàn bộ file `AGENTS.md` này.
2. Đọc `README.md`, và các hướng dẫn gần nhất liên quan đến phần đang sửa.
3. Kiểm tra `git status --short`, nhánh hiện tại và các thay đổi chưa commit.
4. Đọc các file mã nguồn và test liên quan trước khi đưa ra kế hoạch.
5. Nêu ngắn gọn giả định, kế hoạch và tiêu chí xác minh trước khi sửa. Nếu yêu cầu mơ hồ hoặc có nhiều cách hiểu với tác động đáng kể, hỏi người dùng trước; không tự ý chọn cách rủi ro cao.

Không được ghi đè, xóa hoặc hoàn tác thay đổi của người dùng. Không được giả định working tree sạch.

## 2. Mục tiêu và nguyên tắc cốt lõi

- Dự án: **Shawn Studio**, ứng dụng chỉnh sửa video Android.
- Mục tiêu là một ứng dụng Android thực sự hoạt động trên thiết bị, có thể build APK và kiểm tra được; không chỉ là UI giả lập.
- Ưu tiên tính đúng đắn, ổn định, khả năng kiểm thử và thay đổi nhỏ hơn tốc độ thêm tính năng.
- Chỉ triển khai phạm vi người dùng yêu cầu. Không tự thêm tính năng, abstraction, cấu hình hoặc refactor không cần thiết.
- Trước khi code, xác định tiêu chí hoàn thành có thể kiểm chứng.
- Sau khi code, chạy kiểm tra phù hợp và sửa lỗi do thay đổi gây ra trước khi kết thúc.
- Nếu có điều chưa chắc chắn, nêu rõ điều chưa biết và cách xác minh; không che giấu giả định.
- Không tuyên bố tính năng hoạt động trên thiết bị nếu chưa kiểm tra trên thiết bị hoặc emulator.
- Không tuyên bố test/build pass nếu chưa chạy lệnh và xem kết quả thực tế.

## 3. Quy tắc sửa đổi code

- Thực hiện thay đổi nhỏ nhất giải quyết đúng yêu cầu.
- Chỉ sửa file cần thiết; không dọn dẹp hoặc định dạng lại code không liên quan.
- Giữ phong cách, cấu trúc và quy ước hiện có của repo.
- Không thêm dependency mới nếu Android/Kotlin API hiện có đã giải quyết được yêu cầu. Nếu cần dependency, giải thích lý do, kích thước/tác động, giấy phép và lựa chọn thay thế trước khi thêm.
- Không để lại import, biến, hàm hoặc tài nguyên không dùng do chính thay đổi tạo ra.
- Không xóa code cũ chỉ vì thấy có vẻ không dùng nếu không liên quan; báo lại cho người dùng.
- Không dùng TODO, placeholder hoặc dữ liệu giả để giả vờ tính năng đã hoàn thành.
- Không nuốt exception/lỗi một cách im lặng. Hiển thị trạng thái lỗi có ích cho người dùng và ghi log phù hợp, tránh log dữ liệu nhạy cảm.
- Không bắt lỗi các trường hợp bất khả thi chỉ để tạo thêm lớp phức tạp.
- Không sửa cấu hình máy người dùng, PATH, biến môi trường, registry, SDK hoặc cache một cách rộng rãi khi chưa có lý do rõ ràng và sự đồng ý cần thiết.
- Không chạy lệnh phá hủy dữ liệu, xóa cache diện rộng, force reset, force push hoặc ghi đè file người dùng nếu chưa được cho phép rõ ràng.
- Không commit, push, tạo release hoặc phát hành APK thay người dùng nếu chưa được yêu cầu.

## 4. Kiến trúc Shawn Studio

- Giao diện Android dùng **Kotlin + Jetpack Compose**.
- Dùng API Android native phù hợp cho chức năng thiết bị, media picker, playback, quyền truy cập và lưu file.
- **Rust/JNI** dành cho logic native/portable khi có lợi ích kỹ thuật rõ ràng; không chuyển logic sang Rust chỉ để dùng Rust.
- Giữ cầu nối Rust/JNI hiện có hoạt động. Không sửa hoặc thay thế `NativeBridge`, Rust crate hay cấu hình native nếu tính năng không yêu cầu.
- Khi thay đổi JNI/Rust, phải kiểm tra tên symbol, kiểu dữ liệu, ABI và đường build native tương ứng.
- Không giả định mọi điện thoại đều hỗ trợ cùng ABI, codec, độ phân giải hoặc tính năng phần cứng.
- Tách logic thuần khỏi Android framework khi việc đó đơn giản và giúp unit test; không tạo abstraction chỉ để phòng ngừa tương lai.
- Không đưa cả ứng dụng desktop/web vào Android bằng cách sao chép workspace mà chưa chứng minh khả năng tương thích.

## 5. Tận dụng OpenCut

- OpenCut là nguồn tham khảo/tái sử dụng có chọn lọc, không phải yêu cầu phải bê nguyên toàn bộ dự án.
- Giữ kiến trúc Android hiện tại trừ khi có phân tích và lý do kỹ thuật rõ ràng cho việc đổi kiến trúc.
- Ưu tiên tham khảo mô hình dữ liệu, thuật toán timeline và cách biểu diễn thời gian nếu phù hợp.
- Không mặc định mã OpenCut desktop, GPUI, WASM, browser bindings, GPU, effects, masks hoặc compositor có thể chạy trên Android.
- Trước khi sao chép hoặc chỉnh sửa code từ OpenCut:
  1. Xác minh repo/phiên bản/đường dẫn nguồn chính xác.
  2. Đọc giấy phép của repo và các thành phần liên quan.
  3. Kiểm tra dependency, platform assumptions và khả năng tương thích Android.
  4. Chỉ lấy phần nhỏ nhất có ích.
  5. Giữ lại copyright/license notice bắt buộc và ghi nhận nguồn.
- Không tuyên bố đã tích hợp OpenCut nếu chỉ tham khảo ý tưởng.
- Không thêm OpenCut dependency hoặc sao chép code trước khi đánh giá tính tương thích và giấy phép.

## 6. Quy ước phiên bản — BẮT BUỘC

`versionName` dùng định dạng `MAJOR.MINOR.PATCH`.

- **Bản base đầu tiên:** `0.1.0`.
- Mỗi lần thêm một tính năng nhỏ, hoặc hoàn thiện/debug một phần nhỏ của tính năng lớn: tăng PATCH thêm `0.0.1`.
  - Ví dụ: `0.1.0` → `0.1.1`.
- Mỗi lần hoàn thiện một tính năng lớn trong tổng thể ứng dụng: tăng MINOR thêm `0.1.0` và reset PATCH về `0`.
  - Ví dụ: `0.1.3` → `0.2.0`.
- Khi hoàn thiện một phiên bản lớn tổng thể của app: tăng MAJOR thêm `1`, reset MINOR và PATCH về `0`.
  - Ví dụ: `0.9.0` → `1.0.0`.
- Không tự tăng nhiều thành phần cùng lúc nếu quy tắc trên không yêu cầu.
- Trước khi đổi version, đọc `app/build.gradle.kts`, version hiện tại và lịch sử/release context có sẵn. Không đoán version hiện tại.
- `versionCode` Android phải tăng đơn điệu cho mỗi bản APK được đánh số phát hành; không giảm hoặc tái sử dụng số đã phát hành. Chọn giá trị kế tiếp dựa trên cấu hình/lịch sử repo, không tự đặt tùy tiện.
- Mỗi thay đổi cần ghi trong báo cáo cuối: `versionName` cũ → mới và `versionCode` cũ → mới.
- Nếu chưa rõ thay đổi là PATCH, MINOR hay MAJOR, giải thích cách phân loại và hỏi người dùng trước khi đổi version.
- Không tăng version chỉ vì chạy build lại nếu không có thay đổi được xem là bản mới theo quy ước.
- Nếu một bước kỹ thuật chưa hoàn tất hoặc chưa được người dùng chấp nhận, không đánh dấu tính năng lớn là hoàn thành.

## 7. Quy trình bắt buộc trước khi code

1. **Hiểu yêu cầu:** viết lại mục tiêu bằng một câu; liệt kê rõ phần nằm trong và ngoài phạm vi.
2. **Kiểm tra repo:** đọc hướng dẫn, `git status`, code liên quan, test và version hiện tại.
3. **Xác định tiêu chí hoàn thành:** chọn các lệnh/test/thao tác có thể chứng minh kết quả.
4. **Lập kế hoạch ngắn:** nêu file dự kiến sửa và lý do.
5. **Xử lý sự mơ hồ:** hỏi trước nếu lựa chọn có thể ảnh hưởng kiến trúc, dữ liệu người dùng, dependency, giấy phép hoặc tính tương thích.
6. **Sửa code:** triển khai theo thay đổi nhỏ nhất, không mở rộng phạm vi.
7. **Kiểm thử:** chạy test phù hợp, build và kiểm tra diff.
8. **Đánh giá diff:** xác nhận chỉ thay đổi cần thiết, không có secret, file build hoặc thay đổi ngoài ý muốn.
9. **Báo cáo:** nêu chính xác những gì đã sửa, những lệnh đã chạy, kết quả thực tế, hạn chế và cách người dùng kiểm tra.

Với nhiệm vụ nhỏ, có thể gộp các bước trình bày để tránh thủ tục thừa, nhưng không được bỏ qua kiểm tra an toàn và xác minh.

## 8. Kiểm thử và xác minh

- Chọn test để chứng minh hành vi, không chỉ kiểm tra rằng hàm trả về một giá trị không rỗng.
- Với logic thuần (ví dụ giới hạn thời gian clip), thêm unit test cho trường hợp bình thường, biên và đầu vào không hợp lệ khi phù hợp.
- Với Android framework, media picker, playback, codec, permission và xuất file, phân biệt rõ unit test với kiểm thử instrumentation/thiết bị.
- Không tạo test giả hoặc mock rồi dùng nó để tuyên bố chức năng phần cứng đã hoạt động.
- Sau thay đổi Kotlin/Android, chạy các lệnh thích hợp, thường gồm:
  - `.\gradlew.bat testDebugUnitTest`
  - `.\gradlew.bat assembleDebug`
  - `git diff --check`
- Khi sửa Rust, JNI, build script hoặc native dependencies, cần chạy thêm test/build Rust liên quan, ví dụ:
  - `cargo test --manifest-path rust/shawn-native/Cargo.toml`
  - `.\gradlew.bat :app:buildRustArm64 --rerun-tasks`
- Chỉ chạy lệnh nếu công cụ/đường dẫn hiện có. Nếu `cargo`, `adb` hoặc SDK không có trong PATH, kiểm tra đường dẫn cài đặt đã biết trước khi kết luận không thể chạy.
- Không coi task `UP-TO-DATE` là bằng chứng mã native đã được biên dịch lại. Nếu cần xác minh native build mới, chạy task với `--rerun-tasks` và ghi đúng kết quả.
- Nếu lệnh thất bại, báo lệnh và thông báo lỗi cụ thể; không che lỗi bằng cách chỉ trích dẫn một build cũ.
- APK được tạo không đồng nghĩa APK đã chạy được trên điện thoại.
- Khi cần kiểm tra thiết bị, xác nhận ABI và dùng thiết bị/emulator thực tế. Không đoán rằng mọi máy đều là `arm64-v8a`.
- Không tuyên bố test pass nếu chưa thấy exit status/kết quả pass.

## 9. Quy tắc cho chức năng video

### Media picker và URI
- Ưu tiên system picker/SAF như `ActivityResultContracts.OpenDocument` khi phù hợp.
- Không xin quyền đọc toàn bộ thư viện media nếu system picker đã đủ.
- Xử lý URI không đọc được, file bị xóa/di chuyển, MIME type không hợp lệ và quyền truy cập URI hết hạn.
- Nếu tính năng yêu cầu truy cập URI sau khi app/process khởi động lại, đánh giá việc lưu quyền truy cập lâu dài và lưu metadata cần thiết.
- Không giả định URI là đường dẫn filesystem thực; không chuyển URI thành file path bằng cách đoán.

### Playback
- Playback phải có trạng thái rõ ràng: chưa chọn media, đang chuẩn bị, đang phát, đã tạm dừng và lỗi khi phù hợp.
- Xử lý lifecycle và giải phóng tài nguyên player/view phù hợp.
- Không tuyên bố mọi container/codec đều hỗ trợ; xác minh trên thiết bị và thông báo lỗi dễ hiểu.
- Khi thay đổi player, giữ luồng chọn video hiện tại hoạt động trừ khi yêu cầu thay đổi rõ ràng.

### Thời gian, trim và timeline
- Xác định một đơn vị thời gian nhất quán, chẳng hạn milliseconds cho API Android; tránh trộn milliseconds, seconds và frame ticks mà không chuyển đổi rõ ràng.
- Đảm bảo giới hạn hợp lệ: `0 <= start < end <= duration`.
- Xử lý video thời lượng 0/chưa sẵn sàng, clip rất ngắn, điểm đầu/cuối ở biên và seek không chính xác do giới hạn player.
- Dùng số nguyên/độ chính xác phù hợp; tránh sai số floating-point không cần thiết khi lưu vị trí thời gian.
- Phân biệt thời gian media gốc với thời gian trên timeline nếu có nhiều clip.
- Không thêm mô hình timeline phức tạp trước khi chức năng đơn clip hoạt động và có test.
- Với split/trim, định nghĩa rõ quy tắc đầu/cuối, rounding và frame boundary trước khi triển khai.

### Xuất video
- Preview thành công không chứng minh trim/export thành công.
- Trước khi chọn giải pháp xuất, kiểm tra API Android, khả năng codec/encoder, hỗ trợ thiết bị, giấy phép và dependency.
- Xác minh thời lượng, định dạng, đồng bộ audio/video khi có âm thanh, tính toàn vẹn file và khả năng phát lại đầu ra.
- Xử lý tiến trình, hủy tác vụ, lỗi, thiếu dung lượng và vị trí lưu file phù hợp.
- Không ghi đè file người dùng bất ngờ; không xóa nguồn.
- Ưu tiên giải pháp đơn giản đáp ứng yêu cầu; không thêm FFmpeg hoặc thư viện native nặng nếu chưa chứng minh cần thiết và xem xét giấy phép.

## 10. Quy tắc dependency, bảo mật và dữ liệu

- Dùng phiên bản dependency tương thích với Gradle, Kotlin, Compose, SDK và minSdk hiện tại.
- Trước khi thêm dependency, xác minh nguồn, license, kích thước, hỗ trợ Android/ABI và bảo trì.
- Không đưa token, mật khẩu, keystore, khóa ký, thông tin cá nhân hoặc file cấu hình máy riêng vào Git.
- Không commit `local.properties`, cache Gradle/Cargo, build outputs hoặc thư viện native sinh tự động nếu repo hiện tại đã ignore chúng.
- Không sửa `.gitignore` để buộc commit artifact build chỉ nhằm làm build thành công.
- Không dùng dữ liệu người dùng/video cá nhân cho logging hoặc test nếu không cần thiết.
- Giữ quyền Android ở mức tối thiểu cần thiết.
- Trước thao tác có thể mất dữ liệu, hỏi người dùng và đề xuất backup.

## 11. Git và tính toàn vẹn repo

- Bắt đầu bằng `git status --short`; ghi nhận thay đổi có sẵn.
- Không reset, checkout đè, stash, rebase hoặc sửa lịch sử để xóa thay đổi của người dùng nếu chưa được yêu cầu.
- Không commit hoặc push nếu người dùng chưa yêu cầu.
- Trước khi hoàn tất, xem `git diff --check` và `git diff --stat`; xem nội dung diff khi cần.
- Đảm bảo chỉ file thuộc phạm vi yêu cầu bị sửa.
- Không tuyên bố remote GitHub đã cập nhật nếu chưa thực sự push và xác minh.
- Nếu có thay đổi ngoài ý muốn, dừng và giải thích thay vì tự động dọn sạch.

## 12. Quy ước build và môi trường Windows

- Dự án thường được thao tác trên Windows bằng PowerShell và `.\gradlew.bat`.
- Không giả định `cargo`, `adb` hoặc các công cụ Android đã có trong PATH.
- Khi không tìm thấy lệnh, kiểm tra vị trí cài đặt đã biết trước khi thay đổi PATH hoặc cài lại công cụ.
- SDK hiện được cấu hình tại `D:\dev\apps\Android\Sdk` trên máy phát triển đã được ghi nhận; coi đây là thông tin môi trường, không hardcode đường dẫn máy cá nhân vào source code.
- `local.properties` là file cục bộ, không commit.
- Trước khi đề nghị xóa cache hoặc cài lại SDK/NDK/JDK/Rust, hãy xác định nguyên nhân và ưu tiên giải pháp ít rủi ro nhất.
- Khi build native, kiểm tra target ABI được cấu hình trong Gradle và ABI mà thiết bị hỗ trợ.

## 13. Báo cáo hoàn tất — bắt buộc

Khi kết thúc một nhiệm vụ code, báo cáo bằng tiếng Việt, ngắn gọn nhưng đầy đủ:

1. **Kết quả:** đã làm gì và chưa làm gì.
2. **File thay đổi:** đường dẫn và mục đích từng file.
3. **Version:** `versionName`/`versionCode` trước và sau, kèm lý do phân loại PATCH/MINOR/MAJOR.
4. **Kiểm thử:** từng lệnh đã chạy và kết quả thật (`PASS`, `FAIL`, `NOT RUN`); ghi lý do nếu không chạy.
5. **Build:** APK có được tạo mới hay chỉ còn artifact cũ; nêu đúng đường dẫn nếu đã xác minh tồn tại.
6. **Thiết bị:** nói rõ đã kiểm thử trên emulator/điện thoại hay chưa.
7. **Hạn chế/rủi ro:** chỉ ra điều chưa xác minh.
8. **Bước tiếp theo:** đề xuất một bước kế tiếp nhỏ nhất phù hợp.

Không được:
- Nói “hoàn tất” nếu tiêu chí chấp nhận chưa đạt.
- Gộp `NOT RUN` thành `PASS`.
- Dùng build thành công của lần trước để chứng minh thay đổi hiện tại đã build.
- Tuyên bố app hoạt động thực tế chỉ dựa vào unit test.
- Lược bỏ lỗi quan trọng khỏi báo cáo.

## 14. Tiêu chí chung để coi một tính năng hoàn thành

Một tính năng chỉ được coi là hoàn thành khi:
- Phạm vi và tiêu chí chấp nhận đã được xác định.
- Luồng thành công hoạt động.
- Các lỗi/biên quan trọng đã được xử lý hoặc được ghi rõ là giới hạn.
- Test phù hợp đã chạy và kết quả được báo trung thực.
- Build liên quan thành công.
- Kiểm thử thiết bị được thực hiện nếu tính năng phụ thuộc hành vi Android/media/codec/phần cứng; nếu chưa, phải ghi là chưa xác minh.
- Không có thay đổi ngoài phạm vi hoặc lỗi hồi quy đã biết.
- Version được cập nhật đúng quy ước khi bản thay đổi được xác định là bản phát hành.
- Người dùng được cung cấp cách kiểm tra rõ ràng.

## 15. Device Testing, User Approval, and Version Bump

After completing an implementation milestone, follow this workflow strictly:

1. **Verify the implementation:** Run the relevant unit tests, build, and `git diff --check`. Fix any failures before proceeding.
2. **Install and test on a real device:** Install the newly built APK on the connected Android device and test the features affected by the changes. Do not treat a successful build as proof that the feature works.
3. **Report the results:** Summarize the changes, files affected, test/build results, device test results, and any known limitations. Clearly distinguish verified results from tests that were not performed.
4. **Stop and wait for user approval:** Once implementation and device testing are complete, report the milestone as ready for review and wait for explicit approval. Do not proceed with a version bump before approval.
5. **Bump the version only after approval:** After the user explicitly approves the milestone, update the app version according to the existing versioning rules. Update `versionCode` as required, and verify the version changes with the relevant build and checks.
6. **Do not bundle unrelated actions into approval:** Approval to complete a milestone authorizes the version bump only as specified above. It does not authorize committing, pushing, or performing unrelated changes unless explicitly requested.
7. **If device testing is blocked:** Do not claim the milestone is fully verified or bump the version. Explain the blocker, report which checks passed, and wait for the user to help resolve it.

**Required workflow:**

`Implement → Unit tests → Build and diff checks → Install on device → Device testing → Report results → Wait for user approval → Bump version → Verify version change`

The version must not be bumped before explicit user approval, even if all tests pass. All existing versioning rules remain in effect.

---

**Nguyên tắc ưu tiên cao nhất:** bảo vệ thay đổi của người dùng, giữ kiến trúc đơn giản, chỉ làm đúng phạm vi yêu cầu, và xác minh mọi tuyên bố bằng kết quả thực tế.
