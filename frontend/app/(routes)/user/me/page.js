/**
 * 사용자가 본인 정보 보는 페이지.
 */

const fetchMyInfo = async () => {
    const response = await fetch("/api/user", {
        method: "GET",
    });

    const data = await response.json();
    if (response.ok) {
        console.log("내 정보", data);
    } else {
        console.error("정보 조회 실패", data.error);
    }
};
