export async function POST(req) {
    try {
        //백엔드와 연결하는 route 파일
        const { type, value } = await req.json();

        //더미 데이터 (실제 DB와 연동해야 함)
        const existingUsers = ["testuser", "admin", "user123"];
        const existingEmails = ["test@example.com", "admin@example.com"];

        let exists = false;
        console.log("type=> " + type, "value => " + value);
        if (type === "username") {
            exists = existingUsers.includes(value);

        } else if (type === "email") {
            exists = existingEmails.includes(value);

        } else {
            return new Response(JSON.stringify({ error: "잘못된 요청입니다." }), { status: 400 });
        }

        return new Response(JSON.stringify({ exists }), { status: 200 });
    } catch (error) {
        console.error("❌ 중복 확인 API 오류:", error);
        return new Response(JSON.stringify({ error: "서버 오류 발생" }), { status: 500 });
    }
}
