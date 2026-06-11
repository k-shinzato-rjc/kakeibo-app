"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";

// カテゴリー セレクトボックス表示用
type Category = { id: number; name: string; type: string };

// フォームデータの型
type Transaction = {
  transactionDate: string;
  amount: number;
  categoryId: number;
  memo: string;
};

const springURL = "http://localhost:8080";

// 収支入力画面
export default function InputView() {
  const router = useRouter();
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm<Transaction>();
  const [categories, setCategories] = useState<Category[]>([]);

  // 取引カテゴリー セレクトボックス表示のためデータ取得
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(springURL + "/api/categories");
        if (response.ok) setCategories(await response.json());
      } catch (error) { console.error(error); }
    };
    fetchData();
  }, []);

  // 入力フォームをSpringBootに転送（POST通信）
  const Submit = async (data: Transaction) => {
    if (!window.confirm("この内容で登録してもよろしいですか？")) {
      return;
    }
    try {
      const response = await fetch(springURL + "/api/regist", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
        credentials: "include"
      });

      if (!response.ok) {
        alert("登録に失敗しました。");
      } else {
        alert("登録が完了しました！");
        reset();
      }
    } catch (error) {
      alert("通信エラーが発生しました。");
    }
  };

  return (
    <div className="max-w-[900px] mx-auto my-10 p-10 bg-white/80 backdrop-blur-xl rounded-[32px] shadow-[0_20px_50px_-15px_rgba(79,70,229,0.2)] border border-white font-sans text-slate-800">
      
      <div className="flex justify-between items-center mb-10">
        <div>
          <h1 className="text-4xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-violet-600 mb-2">収支入力</h1>
          <p className="text-slate-500 font-medium">新しい取引を追加します</p>
        </div>
        <button 
          onClick={() => router.push("/")}
          className="px-6 py-3 rounded-2xl bg-slate-100 text-slate-700 font-bold hover:bg-indigo-50 hover:text-indigo-600 transition-all duration-300"
        >
          ダッシュボードに戻る
        </button>
      </div>

      <div className="bg-white p-8 rounded-[24px] border border-slate-100 shadow-inner">
        <form onSubmit={handleSubmit(Submit)} className="space-y-6">
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

            <div>
              <label className="block text-sm font-bold text-slate-600 mb-2 ml-1">日付</label>
              <input type="date" {...register("transactionDate", { required: "日付を選択してください" })} 
                className="w-full p-4 rounded-xl border border-slate-200 focus:ring-4 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all" />
              {errors.transactionDate && <p className="text-rose-500 text-sm mt-1 font-medium">{errors.transactionDate.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-bold text-slate-600 mb-2 ml-1">金額 (円)</label>
              <input type="number" {...register("amount", { valueAsNumber: true, required: "必須です", min: { value: 0, message: "0以上の整数を入力" } })} 
                className="w-full p-4 rounded-xl border border-slate-200 focus:ring-4 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all" placeholder="1000" />
              {errors.amount && <p className="text-rose-500 text-sm mt-1 font-medium">{errors.amount.message}</p>}
            </div>
          </div>

          <div>
            <label className="block text-sm font-bold text-slate-600 mb-2 ml-1">取引カテゴリ</label>
            <select {...register("categoryId", { required: "カテゴリを選択してください", valueAsNumber: true })} 
              className="w-full p-4 rounded-xl border border-slate-200 focus:ring-4 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all bg-white">
              <option value="">カテゴリを選択してください</option>
              {categories?.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
            {errors.categoryId && <p className="text-rose-500 text-sm mt-1 font-medium">{errors.categoryId.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-bold text-slate-600 mb-2 ml-1">メモ</label>
            <textarea {...register("memo")} rows={3}
              className="w-full p-4 rounded-xl border border-slate-200 focus:ring-4 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all" placeholder="メモがあれば入力..." />
          </div>

          <button type="submit" disabled={isSubmitting}
            className="w-full py-4 rounded-2xl bg-gradient-to-r from-indigo-600 to-violet-600 text-white font-bold text-lg shadow-xl shadow-indigo-500/30 hover:shadow-indigo-500/50 hover:-translate-y-0.5 active:scale-[0.98] transition-all duration-300">
            {isSubmitting ? "送信中..." : "収支を登録する"}
          </button>
        </form>
      </div>
    </div>
  );
}