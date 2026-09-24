import 'dart:ui';
import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

/// A frosted-glass card widget using BackdropFilter.
class GlassCard extends StatelessWidget {
  const GlassCard({
    super.key,
    required this.child,
    this.padding = const EdgeInsets.all(20),
    this.borderRadius = 24,
    this.opacity = 0.18,
    this.blurSigma = 12.0,
    this.gradient,
    this.border,
    this.boxShadow,
  });

  final Widget child;
  final EdgeInsetsGeometry padding;
  final double borderRadius;
  final double opacity;
  final double blurSigma;
  final Gradient? gradient;
  final BoxBorder? border;
  final List<BoxShadow>? boxShadow;

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(borderRadius),
      child: BackdropFilter(
        filter: ImageFilter.blur(sigmaX: blurSigma, sigmaY: blurSigma),
        child: Container(
          padding: padding,
          decoration: BoxDecoration(
            gradient: gradient ??
                LinearGradient(
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                  colors: [
                    AppColors.white.withOpacity(opacity + 0.10),
                    AppColors.white.withOpacity(opacity),
                  ],
                ),
            borderRadius: BorderRadius.circular(borderRadius),
            border: border ??
                Border.all(
                  color: AppColors.white.withOpacity(0.35),
                  width: 1.2,
                ),
            boxShadow: boxShadow ??
                [
                  BoxShadow(
                    color: AppColors.primaryEmerald.withOpacity(0.08),
                    blurRadius: 20,
                    spreadRadius: 0,
                    offset: const Offset(0, 8),
                  ),
                  BoxShadow(
                    color: Colors.black.withOpacity(0.04),
                    blurRadius: 10,
                    offset: const Offset(0, 2),
                  ),
                ],
          ),
          child: child,
        ),
      ),
    );
  }
}
