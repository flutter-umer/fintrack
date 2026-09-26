import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../theme/app_theme.dart';
import '../services/auth_service.dart';
import '../widgets/glass_card.dart';
import '../widgets/glass_textfield.dart';
import '../widgets/primary_button.dart';
import '../widgets/social_button.dart';
import 'home_screen.dart';

class SignupScreen extends StatefulWidget {
  const SignupScreen({super.key});

  @override
  State<SignupScreen> createState() => _SignupScreenState();
}

class _SignupScreenState extends State<SignupScreen> {
  // ── Controllers ──────────────────────────────────────────────────────────
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmController = TextEditingController();

  // ── Focus nodes ──────────────────────────────────────────────────────────
  final _nameFocus = FocusNode();
  final _emailFocus = FocusNode();
  final _passwordFocus = FocusNode();
  final _confirmFocus = FocusNode();

  // ── State ────────────────────────────────────────────────────────────────
  bool _isLoading = false;
  String? _nameError;
  String? _emailError;
  String? _passwordError;
  String? _confirmError;
  String? _globalError; // Firebase / network errors

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    _confirmController.dispose();
    _nameFocus.dispose();
    _emailFocus.dispose();
    _passwordFocus.dispose();
    _confirmFocus.dispose();
    super.dispose();
  }

  // ── Validation ───────────────────────────────────────────────────────────

  bool _validate() {
    String? nameErr, emailErr, passErr, confirmErr;

    final name = _nameController.text.trim();
    final email = _emailController.text.trim();
    final pass = _passwordController.text;
    final confirm = _confirmController.text;

    if (name.isEmpty) {
      nameErr = 'Full name is required.';
    } else if (name.length < 2) {
      nameErr = 'Name must be at least 2 characters.';
    }

    if (email.isEmpty) {
      emailErr = 'Email is required.';
    } else if (!RegExp(r'^[^@]+@[^@]+\.[^@]+').hasMatch(email)) {
      emailErr = 'Enter a valid email address.';
    }

    if (pass.isEmpty) {
      passErr = 'Password is required.';
    } else if (pass.length < 6) {
      passErr = 'Password must be at least 6 characters.';
    }

    if (confirm.isEmpty) {
      confirmErr = 'Please confirm your password.';
    } else if (confirm != pass) {
      confirmErr = 'Passwords do not match.';
    }

    setState(() {
      _nameError = nameErr;
      _emailError = emailErr;
      _passwordError = passErr;
      _confirmError = confirmErr;
      _globalError = null;
    });

    return nameErr == null &&
        emailErr == null &&
        passErr == null &&
        confirmErr == null;
  }

  // ── Actions ──────────────────────────────────────────────────────────────

  Future<void> _onCreateAccount() async {
    if (!_validate()) return;
    FocusScope.of(context).unfocus();
    setState(() {
      _isLoading = true;
      _globalError = null;
    });

    final result = await AuthService.instance.signUpWithEmail(
      name: _nameController.text.trim(),
      email: _emailController.text.trim(),
      password: _passwordController.text,
    );

    if (!mounted) return;
    setState(() => _isLoading = false);

    if (result.isSuccess && result.user != null) {
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => HomeScreen(user: result.user!)),
        (_) => false,
      );
    } else {
      setState(() => _globalError = result.errorMessage);
    }
  }

  Future<void> _onGoogleSignUp() async {
    FocusScope.of(context).unfocus();
    setState(() {
      _isLoading = true;
      _globalError = null;
    });

    final result = await AuthService.instance.signInWithGoogle();

    if (!mounted) return;
    setState(() => _isLoading = false);

    if (result.isSuccess && result.user != null) {
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => HomeScreen(user: result.user!)),
        (_) => false,
      );
    } else {
      setState(() => _globalError = result.errorMessage);
    }
  }

  // ── Build ────────────────────────────────────────────────────────────────

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
            child: _Blob(size: 220, color: AppColors.primaryEmerald.withOpacity(0.09)),
          ),
          Positioned(
            bottom: 60,
            left: -60,
            child: _Blob(size: 180, color: AppColors.primaryEmerald.withOpacity(0.06)),
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
                        'Create Account',
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
                        'Start your journey to smarter financial management.',
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
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          // Global error banner
                          if (_globalError != null) ...[
                            _ErrorBanner(message: _globalError!),
                            const SizedBox(height: 20),
                          ],

                          // Full name
                          GlassTextField(
                            label: 'Full Name',
                            hint: 'John Doe',
                            prefixIcon: Icons.person_outline_rounded,
                            controller: _nameController,
                            focusNode: _nameFocus,
                            errorText: _nameError,
                            onChanged: (_) =>
                                setState(() => _nameError = null),
                            textInputAction: TextInputAction.next,
                            onSubmitted: (_) =>
                                FocusScope.of(context).requestFocus(_emailFocus),
                          ),

                          const SizedBox(height: 20),

                          // Email
                          GlassTextField(
                            label: 'Email Address',
                            hint: 'you@example.com',
                            prefixIcon: Icons.mail_outline_rounded,
                            keyboardType: TextInputType.emailAddress,
                            controller: _emailController,
                            focusNode: _emailFocus,
                            errorText: _emailError,
                            onChanged: (_) =>
                                setState(() => _emailError = null),
                            textInputAction: TextInputAction.next,
                            onSubmitted: (_) =>
                                FocusScope.of(context).requestFocus(_passwordFocus),
                          ),

                          const SizedBox(height: 20),

                          // Password
                          GlassTextField(
                            label: 'Password',
                            hint: '••••••••',
                            prefixIcon: Icons.lock_outline_rounded,
                            isPassword: true,
                            controller: _passwordController,
                            focusNode: _passwordFocus,
                            errorText: _passwordError,
                            onChanged: (_) =>
                                setState(() => _passwordError = null),
                            textInputAction: TextInputAction.next,
                            onSubmitted: (_) =>
                                FocusScope.of(context).requestFocus(_confirmFocus),
                          ),

                          const SizedBox(height: 20),

                          // Confirm password
                          GlassTextField(
                            label: 'Confirm Password',
                            hint: '••••••••',
                            prefixIcon: Icons.lock_outline_rounded,
                            isPassword: true,
                            controller: _confirmController,
                            focusNode: _confirmFocus,
                            errorText: _confirmError,
                            onChanged: (_) =>
                                setState(() => _confirmError = null),
                            textInputAction: TextInputAction.done,
                            onSubmitted: (_) => _onCreateAccount(),
                          ),

                          const SizedBox(height: 28),

                          // Create account button
                          PrimaryButton(
                            label: 'Create Account',
                            icon: Icons.arrow_forward_rounded,
                            isLoading: _isLoading,
                            onPressed: _isLoading ? null : _onCreateAccount,
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

                          // Google button (full-width)
                          SocialButton(
                            label: 'Continue with Google',
                            iconWidget: const GoogleIcon(size: 22),
                            onPressed: _isLoading ? null : _onGoogleSignUp,
                          ),
                        ],
                      ),
                    ),

                    const SizedBox(height: 28),

                    // Log in link
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          'Already have an account?  ',
                          style: GoogleFonts.poppins(
                            fontSize: 14,
                            color: AppColors.textSecondary,
                          ),
                        ),
                        GestureDetector(
                          onTap: () => Navigator.of(context).pop(),
                          child: Text(
                            'Log in',
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
// Logo + Brand row (same as LoginScreen)
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
// Red error banner shown above form fields
// ─────────────────────────────────────────────────────────────────────────────

class _ErrorBanner extends StatelessWidget {
  const _ErrorBanner({required this.message});
  final String message;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      decoration: BoxDecoration(
        color: AppColors.errorExpense.withOpacity(0.10),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: AppColors.errorExpense.withOpacity(0.35)),
      ),
      child: Row(
        children: [
          Icon(Icons.error_outline_rounded,
              color: AppColors.errorExpense, size: 18),
          const SizedBox(width: 10),
          Expanded(
            child: Text(
              message,
              style: GoogleFonts.poppins(
                fontSize: 13,
                color: AppColors.errorExpense,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Background blob (same pattern as LoginScreen)
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
