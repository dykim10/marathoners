import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
    root: resolve(__dirname, 'app'),  //root를 'app'으로 설정
    server: {
        port: 3004,  //3004 포트에서 실행
        proxy: {
            "/api": {
                target: "http://localhost:8080",
                changeOrigin: true,
                secure: false
            }
        }
    },
    base: '/',
    build: {
        outDir: resolve(__dirname, 'dist'),
        rollupOptions: {
            input: {
                login: resolve(__dirname, 'app/login/index.html'),
            },
        }
    }
});
