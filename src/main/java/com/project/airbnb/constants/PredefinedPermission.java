package com.project.airbnb.constants;

public class PredefinedPermission {
    public static final String ADMIN = "PERMISSION_ADMIN";
    public static final String HOST = "PERMISSION_HOST";
    public static final String GUEST = "PERMISSION_GUEST";

    // Quản lý danh sách chỗ ở, bao gồm tạo, chỉnh sửa, xóa và cập nhật thông tin.
    public static final String MANAGE_LISTINGS = "MANAGE_LISTINGS";

    // Quản lý đặt chỗ, bao gồm chấp nhận, từ chối, hủy và thay đổi thông tin đặt chỗ.
    public static final String MANAGE_RESERVATIONS = "MANAGE_RESERVATIONS";

    // Quản lý lịch trình, bao gồm cập nhật tình trạng sẵn có và giá cả.
    public static final String MANAGE_CALENDAR = "MANAGE_CALENDAR";

    // Quản lý giá cả, bao gồm thiết lập giá cơ bản, giảm giá và các chương trình khuyến mãi.
    public static final String MANAGE_PRICING = "MANAGE_PRICING";

    // Quản lý đánh giá, bao gồm viết và phản hồi đánh giá từ khách hàng.
    public static final String MANAGE_REVIEWS = "MANAGE_REVIEWS";

    // Quản lý tài chính, bao gồm xem xét và xuất báo cáo doanh thu và thanh toán.
    public static final String MANAGE_FINANCIALS = "MANAGE_FINANCIALS";

    // Quản lý nhóm, bao gồm mời, xóa và phân quyền cho các thành viên trong nhóm.
    public static final String PERMISSION_MANAGE_TEAM = "MANAGE_TEAM";

    // Quản lý vé hỗ trợ, bao gồm tạo, theo dõi và giải quyết các vấn đề của khách hàng.
    public static final String MANAGE_SUPPORT_TICKETS = "MANAGE_SUPPORT_TICKETS";

    private PredefinedPermission(){}
}
