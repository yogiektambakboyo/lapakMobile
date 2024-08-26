package com.lapakkreatiflamongan.smdsforce.utils;

public class NetworkUtils {

    public static String code(String code) {
        String result = "";
        if (code.startsWith("5")) {
            result += "Server Bermasalah.\nHarap hubungi IT dan jelaskan masalah Anda.\n";
        } else if (code.startsWith("4")) {
            result += "Koneksi terputus.\nHarap hubungi customer service dan jelaskan masalah Anda.";
        }
        switch (code) {
            case "505":
                result += "Versi Http tidak mendukung";
                break;
            case "504":
                result += "Gateway Timeout";
                break;
            case "503":
                result += "Layanan tidak tersedia";
                break;
            case "502":
                result += "Bad Gateway";
                break;
            case "501":
                result += "Tidak Diimplementasikan";
                break;
            case "500":
                result += "Kesalahan server";
                break;
            case "417":
                result += "Ekspektasi Gagal";
                break;
            case "416":
                result += "Kisaran yang Diminta Tidak Memuaskan";
                break;
            case "415":
                result += "Unsupported Media Type";
                break;
            case "414":
                result += "Jenis Media Tidak Didukung";
                break;
            case "413":
                result += "Minta Entitas Terlalu Besar";
                break;
            case "412":
                result += "Prasyarat Gagal";
                break;
            case "411":
                result += "Panjang Diperlukan";
                break;
            case "410":
                result += "Hilang";
                break;
            case "409":
                result += "Konflik";
                break;
            case "408":
                result += "Minta Waktu Tunggu";
                break;
            case "407":
                result += "Diperlukan Autentikasi Proksi";
                break;
            case "406":
                result += "Tidak dapat diterima";
                break;
            case "405":
                result += "Metode Tidak Diizinkan";
                break;
            case "404":
                result += "Tidak ditemukan\n";
                break;
            case "403":
                result += "Terlarang";
                break;
            case "402":
                result += "Pembayaran Diperlukan";
                break;
            case "401":
                result += "Tidak sah";
                break;
            case "400":
                result += "Permintaan yang buruk\n";
                break;
            case "900":
                result = "Tidak Ada Koneksi Internet";
                break;
            default:
                result = "Masalah Koneksi Tidak Dikenal";
                break;
        }
        result += "";
        return result;
    }
}