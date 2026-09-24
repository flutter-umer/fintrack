import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../theme/app_theme.dart';
import '../widgets/glass_card.dart';
import '../widgets/glass_textfield.dart';
import '../widgets/primary_button.dart';
import '../widgets/social_button.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: Stack(
        children: [
          // ── Background blobs ────────────────────────────────────────────
          Positioned(
            top: -60,
            right: -40,
            child: _Blob(
              size: 220,
              color: AppColors.primaryEmerald.withOpacity(0.09),
            ),
          ),
          Positioned(
            bottom: 60,
            left: -60,
            child: _Blob(
              size: 180,
              color: AppColors.primaryEmerald.withOpacity(0.06),
            ),
          ),

          // ── Content ─────────────────────────────────────────────────────
          SafeArea(
            child: SingleChildScrollView(
              physics: const BouncingScrollPhysics(),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const SizedBox(height: 36),

                    // Logo + app name
                    _LogoBrand(),

                    const SizedBox(height: 36),

                    // Heading
                    Align(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        'Welcome Back 👋',
                        style: GoogleFonts.poppins(
                          fontSize: 28,
                          fontWeight: FontWeight.w800,
                          color: AppColors.textPrimary,
                        ),
                      ),
                    ),
                    const SizedBox(height: 6),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        'Log in to continue your financial journey.',
                        style: GoogleFonts.poppins(
                          fontSize: 14,
                          color: AppColors.textSecondary,
                        ),
                      ),
                    ),

                    const SizedBox(height: 32),

                    // ── Form card ──────────────────────────────────────────
                    GlassCard(
                      padding: const EdgeInsets.all(24),
                      borderRadius: 24,
                      opacity: 0.08,
                      blurSigma: 6,
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          Colors.white.withOpacity(0.92),
                          Colors.white.withOpacity(0.80),
                        ],
                      ),
                      border: Border.all(
                        color: const Color(0xFFE8EDF3),
                        width: 1.0,
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.05),
                          blurRadius: 24,
                          offset: const Offset(0, 8),
                        ),
                      ],
                      child: Column(
                        children: [
                          // Email field
                          GlassTextField(
                            label: 'Email Address',
                            hint: 'umer@example.com',
                            prefixIcon: Icons.mail_outline_rounded,
                            keyboardType: TextInputType.emailAddress,
                          ),

                          const SizedBox(height: 20),

                          // Password field
                          GlassTextField(
                            label: 'Password',
                            hint: '••••••••••••••',
                            prefixIcon: Icons.lock_outline_rounded,
                            isPassword: true,
                          ),

                          const SizedBox(height: 12),

                          // Forgot password
                          Align(
                            alignment: Alignment.centerRight,
                            child: GestureDetector(
                              onTap: () {},
                              child: Text(
                                'Forgot Password?',
                                style: GoogleFonts.poppins(
                                  fontSize: 13,
                                  fontWeight: FontWeight.w600,
                                  color: AppColors.primaryEmerald,
                                ),
                              ),
                            ),
                          ),

                          const SizedBox(height: 24),

                          // Login button + biometric row
                          Row(
                            children: [
                              Expanded(
                                child: PrimaryButton(
                                  label: 'Login',
                                  icon: Icons.arrow_forward_rounded,
                                  onPressed: () {},
                                ),
                              ),
                              const SizedBox(width: 12),
                              // Biometric button
                              _BiometricButton(),
                            ],
                          ),

                          const SizedBox(height: 24),

                          // Divider
                          Row(
                            children: [
                              Expanded(
                                child: Divider(
                                  color: const Color(0xFFDDE3EB),
                                  thickness: 1,
                                ),
                              ),
                              Padding(
                                padding:
                                    const EdgeInsets.symmetric(horizontal: 14),
                                child: Text(
                                  'or continue with',
                                  style: GoogleFonts.poppins(
                                    fontSize: 12,
                                    color: AppColors.textSecondary,
                                  ),
                                ),
                              ),
                              Expanded(
                                child: Divider(
                                  color: const Color(0xFFDDE3EB),
                                  thickness: 1,
                                ),
                              ),
                            ],
                          ),

                          const SizedBox(height: 20),

                          // Social buttons
                          Row(
                            children: [
                              Expanded(
                                child: SocialButton(
                                  label: 'Apple',
                                  icon: Icons.apple_rounded,
                                  onPressed: () {},
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: SocialButton(
                                  label: 'Google',
                                  iconWidget: const GoogleIcon(size: 22),
                                  onPressed: () {},
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),

                    const SizedBox(height: 28),

                    // Sign up link
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          "Don't have an account?  ",
                          style: GoogleFonts.poppins(
                            fontSize: 14,
                            color: AppColors.textSecondary,
                          ),
                        ),
                        GestureDetector(
                          onTap: () {},
                          child: Text(
                            'Sign Up',
                            style: GoogleFonts.poppins(
                              fontSize: 14,
                              fontWeight: FontWeight.w700,
                              color: AppColors.primaryEmerald,
                            ),
                          ),
                        ),
                      ],
                    ),

                    const SizedBox(height: 32),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Logo + Brand row
// ─────────────────────────────────────────────────────────────────────────────

class _LogoBrand extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: Image.asset(
            'assets/images/logo.jpg',
            width: 44,
            height: 44,
            fit: BoxFit.cover,
          ),
        ),
        const SizedBox(width: 10),
        Text(
          'FinTrack',
          style: GoogleFonts.poppins(
            fontSize: 22,
            fontWeight: FontWeight.w700,
            color: AppColors.textPrimary,
          ),
        ),
      ],
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Biometric / Face ID button
// ─────────────────────────────────────────────────────────────────────────────

class _BiometricButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 56,
      height: 56,
      decoration: BoxDecoration(
        color: AppColors.inputFill,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: const Color(0xFFE2E8F0)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: () {},
          borderRadius: BorderRadius.circular(16),
          child: const Icon(
            Icons.face_retouching_natural_rounded,
            color: AppColors.textSecondary,
            size: 26,
          ),
        ),
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Background blob
// ─────────────────────────────────────────────────────────────────────────────

class _Blob extends StatelessWidget {
  const _Blob({required this.size, required this.color});

  final double size;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(shape: BoxShape.circle, color: color),
    );
  }
}
