import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../theme/app_theme.dart';
import '../widgets/glass_card.dart';
import '../widgets/primary_button.dart';
import 'login_screen.dart';

class SplashScreen extends StatelessWidget {
  const SplashScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: Stack(
        children: [
          // ── Subtle background gradient blobs ──────────────────────────────
          Positioned(
            top: -80,
            right: -60,
            child: _GradientBlob(
              size: 280,
              color: AppColors.primaryEmerald.withOpacity(0.12),
            ),
          ),
          Positioned(
            bottom: 100,
            left: -80,
            child: _GradientBlob(
              size: 240,
              color: AppColors.primaryEmerald.withOpacity(0.08),
            ),
          ),

          // ── Main content ──────────────────────────────────────────────────
          SafeArea(
            child: SingleChildScrollView(
              physics: const BouncingScrollPhysics(),
              padding: const EdgeInsets.symmetric(horizontal: 24),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 36),

                  // Logo + brand name
                  _LogoBrand(),

                  const SizedBox(height: 40),

                  // Hero heading
                  Text(
                    'Take control of\nyour money.',
                    textAlign: TextAlign.center,
                    style: GoogleFonts.poppins(
                      fontSize: 34,
                      fontWeight: FontWeight.w800,
                      color: AppColors.textPrimary,
                      height: 1.2,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    'Track. Manage. Grow.',
                    textAlign: TextAlign.center,
                    style: GoogleFonts.poppins(
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                      color: AppColors.textSecondary,
                      letterSpacing: 0.4,
                    ),
                  ),

                  const SizedBox(height: 32),

                  // Dashboard glass card
                  _DashboardCard(),

                  const SizedBox(height: 28),

                  // Feature chips
                  _FeatureChips(),

                  const SizedBox(height: 36),

                  // CTA button
                  PrimaryButton(
                    label: 'Get Started',
                    onPressed: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (_) => const LoginScreen(),
                        ),
                      );
                    },
                  ),

                  const SizedBox(height: 20),

                  // Already have account
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        'Already have an account? ',
                        style: GoogleFonts.poppins(
                          fontSize: 14,
                          color: AppColors.textSecondary,
                        ),
                      ),
                      GestureDetector(
                        onTap: () => Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (_) => const LoginScreen(),
                          ),
                        ),
                        child: Text(
                          'Log in',
                          style: GoogleFonts.poppins(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
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
        ],
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Logo + Brand Name
// ─────────────────────────────────────────────────────────────────────────────

class _LogoBrand extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // Logo
        ClipRRect(
          borderRadius: BorderRadius.circular(20),
          child: Image.asset(
            'assets/images/logo.jpg',
            width: 72,
            height: 72,
            fit: BoxFit.cover,
          ),
        ),
        const SizedBox(height: 10),
        Text(
          'FINTRACK WEALTH 2.0',
          style: GoogleFonts.poppins(
            fontSize: 11,
            fontWeight: FontWeight.w700,
            letterSpacing: 2.5,
            color: AppColors.primaryEmerald,
          ),
        ),
      ],
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dashboard Glass Card
// ─────────────────────────────────────────────────────────────────────────────

class _DashboardCard extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(26),
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            AppColors.primaryEmerald,
            AppColors.darkEmerald,
            const Color(0xFF045A52),
          ],
          stops: const [0.0, 0.55, 1.0],
        ),
        boxShadow: [
          BoxShadow(
            color: AppColors.primaryEmerald.withOpacity(0.40),
            blurRadius: 30,
            offset: const Offset(0, 14),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(26),
        child: BackdropFilter(
          filter: ImageFilter.blur(sigmaX: 0, sigmaY: 0),
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Header row
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Net Cashflow',
                          style: GoogleFonts.poppins(
                            fontSize: 12,
                            color: Colors.white.withOpacity(0.75),
                            letterSpacing: 0.5,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          '\$12,840.00',
                          style: GoogleFonts.poppins(
                            fontSize: 28,
                            fontWeight: FontWeight.w700,
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
                    // Savings badge
                    Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 14, vertical: 8),
                      decoration: BoxDecoration(
                        color: Colors.white.withOpacity(0.18),
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(
                          color: Colors.white.withOpacity(0.30),
                        ),
                      ),
                      child: Column(
                        children: [
                          Text(
                            '+24.8%',
                            style: GoogleFonts.poppins(
                              fontSize: 15,
                              fontWeight: FontWeight.w700,
                              color: Colors.white,
                            ),
                          ),
                          Text(
                            'Savings',
                            style: GoogleFonts.poppins(
                              fontSize: 10,
                              color: Colors.white.withOpacity(0.80),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),

                const SizedBox(height: 24),

                // Mini line chart
                _MiniLineChart(),

                const SizedBox(height: 20),

                // Stats row
                Row(
                  children: [
                    _StatChip(label: 'Income', value: '\$18,200', positive: true),
                    const SizedBox(width: 12),
                    _StatChip(
                        label: 'Expenses', value: '\$5,360', positive: false),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _StatChip extends StatelessWidget {
  const _StatChip({
    required this.label,
    required this.value,
    required this.positive,
  });

  final String label;
  final String value;
  final bool positive;

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
        decoration: BoxDecoration(
          color: Colors.white.withOpacity(0.14),
          borderRadius: BorderRadius.circular(14),
          border: Border.all(color: Colors.white.withOpacity(0.22)),
        ),
        child: Row(
          children: [
            Icon(
              positive ? Icons.arrow_upward_rounded : Icons.arrow_downward_rounded,
              color: positive ? const Color(0xFF6EF5D8) : const Color(0xFFFF8B86),
              size: 16,
            ),
            const SizedBox(width: 6),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: GoogleFonts.poppins(
                    fontSize: 10,
                    color: Colors.white.withOpacity(0.70),
                  ),
                ),
                Text(
                  value,
                  style: GoogleFonts.poppins(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                    color: Colors.white,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Mini Line Chart (canvas-drawn)
// ─────────────────────────────────────────────────────────────────────────────

class _MiniLineChart extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 60,
      child: CustomPaint(
        painter: _LineChartPainter(),
        size: const Size(double.infinity, 60),
      ),
    );
  }
}

class _LineChartPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    // Data points (normalised 0-1)
    const points = [0.6, 0.4, 0.7, 0.35, 0.8, 0.55, 0.9, 0.65, 0.85];
    final w = size.width;
    final h = size.height;
    final step = w / (points.length - 1);

    List<Offset> offsets = List.generate(
      points.length,
      (i) => Offset(i * step, h - points[i] * h * 0.85 - h * 0.05),
    );

    // Gradient fill
    final fillPath = Path()..moveTo(offsets.first.dx, h);
    for (int i = 0; i < offsets.length - 1; i++) {
      final cp1 = Offset((offsets[i].dx + offsets[i + 1].dx) / 2, offsets[i].dy);
      final cp2 =
          Offset((offsets[i].dx + offsets[i + 1].dx) / 2, offsets[i + 1].dy);
      fillPath.cubicTo(
          cp1.dx, cp1.dy, cp2.dx, cp2.dy, offsets[i + 1].dx, offsets[i + 1].dy);
    }
    fillPath
      ..lineTo(offsets.last.dx, h)
      ..close();

    final fillPaint = Paint()
      ..shader = LinearGradient(
        begin: Alignment.topCenter,
        end: Alignment.bottomCenter,
        colors: [
          Colors.white.withOpacity(0.30),
          Colors.white.withOpacity(0.00),
        ],
      ).createShader(Rect.fromLTWH(0, 0, w, h));
    canvas.drawPath(fillPath, fillPaint);

    // Line
    final linePath = Path()..moveTo(offsets.first.dx, offsets.first.dy);
    for (int i = 0; i < offsets.length - 1; i++) {
      final cp1 = Offset((offsets[i].dx + offsets[i + 1].dx) / 2, offsets[i].dy);
      final cp2 =
          Offset((offsets[i].dx + offsets[i + 1].dx) / 2, offsets[i + 1].dy);
      linePath.cubicTo(
          cp1.dx, cp1.dy, cp2.dx, cp2.dy, offsets[i + 1].dx, offsets[i + 1].dy);
    }
    final linePaint = Paint()
      ..color = Colors.white.withOpacity(0.90)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 2.2
      ..strokeCap = StrokeCap.round;
    canvas.drawPath(linePath, linePaint);

    // End dot
    canvas.drawCircle(
      offsets.last,
      4.5,
      Paint()..color = Colors.white,
    );
  }

  @override
  bool shouldRepaint(covariant CustomPainter old) => false;
}

// ─────────────────────────────────────────────────────────────────────────────
// Feature Chips
// ─────────────────────────────────────────────────────────────────────────────

class _FeatureChips extends StatelessWidget {
  static const _chips = [
    ('Smart Budgets', Icons.pie_chart_outline_rounded),
    ('Real-time Tracking', Icons.show_chart_rounded),
    ('Automated Insights', Icons.auto_awesome_rounded),
  ];

  @override
  Widget build(BuildContext context) {
    return Wrap(
      spacing: 10,
      runSpacing: 10,
      alignment: WrapAlignment.center,
      children: _chips.map((chip) => _FeatureChip(chip.$1, chip.$2)).toList(),
    );
  }
}

class _FeatureChip extends StatelessWidget {
  const _FeatureChip(this.label, this.icon);

  final String label;
  final IconData icon;

  @override
  Widget build(BuildContext context) {
    return GlassCard(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
      borderRadius: 50,
      opacity: 0.06,
      blurSigma: 8,
      border: Border.all(
        color: AppColors.primaryEmerald.withOpacity(0.25),
        width: 1.0,
      ),
      boxShadow: [
        BoxShadow(
          color: AppColors.primaryEmerald.withOpacity(0.06),
          blurRadius: 12,
          offset: const Offset(0, 4),
        ),
      ],
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 15, color: AppColors.primaryEmerald),
          const SizedBox(width: 6),
          Text(
            label,
            style: GoogleFonts.poppins(
              fontSize: 13,
              fontWeight: FontWeight.w500,
              color: AppColors.textPrimary,
            ),
          ),
        ],
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Gradient Blob
// ─────────────────────────────────────────────────────────────────────────────

class _GradientBlob extends StatelessWidget {
  const _GradientBlob({required this.size, required this.color});

  final double size;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: color,
      ),
    );
  }
}
