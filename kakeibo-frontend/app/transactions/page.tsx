"use client";

import React from "react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";

// 取引履歴データ 型
type Transaction = {
    transactionDate: string;
    amount: number;
    categoryId: number;
    memo: string;
  };

export default function HistoryView(){

    const[ transactions, setTransactions] = useState<Transaction[]>();
    return(
        <div>履歴一覧</div>
    );
}