import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

/// FinTrack color palette
class AppColors {
  AppColors._();

  static const Color primaryEmerald = Color(0xFF0F9D8A);
  static const Color darkEmerald = Color(0xFF067A6E);
  static const Color lightMint = Color(0xFFE8FAF6);
  static const Color background = Color(0xFFF6F8FB);
  static const Color textPrimary = Color(0xFF17212B);
  static const Color textSecondary = Color(0xFF6C7A89);
  static const Color errorExpense = Color(0xFFF26A63);
  static const Color white = Color(0xFFFFFFFF);
  static const Color cardBorder = Color(0x33FFFFFF);
  static const Color inputFill = Color(0xFFF0F3F7);
}

/// FinTrack app theme
class AppTheme {
  AppTheme._();

  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      colorScheme: ColorScheme.fromSeed(
        seedColor: AppColors.primaryEmerald,
        brightness: Brightness.light,
      ),
      scaffoldBackgroundColor: AppColors.background,
      textTheme: GoogleFonts.poppinsTextTheme().copyWith(
        displayLarge: GoogleFonts.poppins(
          fontSize: 32,
          fontWeight: FontWeight.w700,
          color: AppColors.textPrimary,
        ),
        displayMedium: GoogleFonts.poppins(
          fontSize: 28,
          fontWeight: FontWeight.w700,
          color: AppColors.textPrimary,
        ),
        headlineLarge: GoogleFonts.poppins(
          fontSize: 24,
          fontWeight: FontWeight.w700,
          color: AppColors.textPrimary,
        ),
        headlineMedium: GoogleFonts.poppins(
          fontSize: 20,
          fontWeight: FontWeight.w600,
          color: AppColors.textPrimary,
        ),
        bodyLarge: GoogleFonts.poppins(
          fontSize: 16,
          fontWeight: FontWeight.w400,
          color: AppColors.textPrimary,
        ),
        bodyMedium: GoogleFonts.poppins(
          fontSize: 14,
          fontWeight: FontWeight.w400,
          color: AppColors.textSecondary,
        ),
        bodySmall: GoogleFonts.poppins(
          fontSize: 12,
          fontWeight: FontWeight.w400,
          color: AppColors.textSecondary,
        ),
        labelLarge: GoogleFonts.poppins(
          fontSize: 16,
          fontWeight: FontWeight.w600,
          color: AppColors.white,
        ),
      ),
    );
  }
}
