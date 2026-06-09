FROM node:20-alpine
WORKDIR /app

# 先にパッケージ情報をコピーしてインストール
COPY package*.json ./
RUN npm install

# ソースコードをコピー
COPY . .

# 開発サーバーを起動
CMD ["npm", "run", "dev"]