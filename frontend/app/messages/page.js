"use client";

import { useState, useEffect } from "react";
import axios from "axios";

export default function MessagesPage() {
    const [messages, setMessages] = useState([]);  // 메시지 목록 상태
    const [newMessage, setNewMessage] = useState("");  // 입력할 새 메시지

    // ✅ API 호출하여 메시지 목록 가져오기
    useEffect(() => {
        fetchMessages();
    }, []);

    const fetchMessages = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/messages");
            setMessages(response.data);
        } catch (error) {
            console.error("메시지를 불러오는 중 오류 발생:", error);
        }
    };

    // ✅ 메시지 추가 함수
    const addMessage = async () => {
        if (!newMessage.trim()) return;

        try {
            await axios.post("http://localhost:8080/api/messages", { message: newMessage });
            setNewMessage(""); // 입력 필드 초기화
            fetchMessages(); // 메시지 목록 갱신
        } catch (error) {
            console.error("메시지를 추가하는 중 오류 발생:", error);
        }
    };

    // ✅ 메시지 삭제 함수
    const deleteMessage = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/messages/${id}`);
            fetchMessages(); // 메시지 목록 갱신
        } catch (error) {
            console.error("메시지를 삭제하는 중 오류 발생:", error);
        }
    };

    return (
        <div className="container">
            <h1>메시지 목록</h1>

            {/* 메시지 입력 폼 */}
            <div>
                <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="새 메시지를 입력하세요"
                />
                <button onClick={addMessage}>추가</button>
            </div>

            {/* 메시지 목록 */}
            <ul>
                {messages.map((msg) => (
                    <li key={msg.id}>
                        {msg.message}
                        <button onClick={() => deleteMessage(msg.id)}>삭제</button>
                    </li>
                ))}
            </ul>

            <style jsx>{`
        .container {
          max-width: 600px;
          margin: 50px auto;
          padding: 20px;
          border: 1px solid #ccc;
          border-radius: 5px;
          text-align: center;
        }
        input {
          padding: 8px;
          margin-right: 5px;
          border: 1px solid #ddd;
          border-radius: 4px;
        }
        button {
          padding: 8px;
          border: none;
          background-color: #007bff;
          color: white;
          cursor: pointer;
          border-radius: 4px;
        }
        button:hover {
          background-color: #0056b3;
        }
        ul {
          list-style: none;
          padding: 0;
        }
        li {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 10px;
          border-bottom: 1px solid #ddd;
        }
      `}</style>
        </div>
    );
}
