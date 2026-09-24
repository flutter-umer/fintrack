import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import '../theme/app_theme.dart';

/// A rounded glass social login button (Apple / Google).
class SocialButton extends StatelessWidget {
  const SocialButton({
    super.key,
    required this.label,
    required this.onPressed,
    this.icon,
    this.iconWidget,
  }) : assert(icon != null || iconWidget != null,
            'Provide either icon or iconWidget');

  final String label;
  final VoidCallback? onPressed;

  /// Material icon (e.g. for Apple)
  final IconData? icon;

  /// Custom icon widget (e.g. a coloured Google "G" built from [Text])
  final Widget? iconWidget;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 52,
      decoration: BoxDecoration(
        color: AppColors.white,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: const Color(0xFFE2E8F0), width: 1.0),
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
          onTap: onPressed,
          borderRadius: BorderRadius.circular(14),
          splashColor: AppColors.lightMint.withOpacity(0.5),
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                if (iconWidget != null)
                  iconWidget!
                else
                  Icon(icon, size: 22, color: AppColors.textPrimary),
                const SizedBox(width: 10),
                Text(
                  label,
                  style: GoogleFonts.poppins(
                    fontSize: 14,
                    fontWeight: FontWeight.w500,
                    color: AppColors.textPrimary,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

/// Inline coloured Google "G" icon built purely with [Text].
class GoogleIcon extends StatelessWidget {
  const GoogleIcon({super.key, this.size = 20});

  final double size;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: size,
      height: size,
      child: CustomPaint(painter: _GoogleGPainter()),
    );
  }
}

class _GoogleGPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final double cx = size.width / 2;
    final double cy = size.height / 2;
    final double r = size.width / 2;

    // Draw quadrants of the "G"
    final segments = [
      // top-right: red  (#EA4335)
      _Segment(0, 90, const Color(0xFFEA4335)),
      // bottom-right: yellow (#FBBC05)
      _Segment(90, 180, const Color(0xFFFBBC05)),
      // bottom-left: green (#34A853)
      _Segment(180, 270, const Color(0xFF34A853)),
      // top-left: blue (#4285F4)
      _Segment(270, 360, const Color(0xFF4285F4)),
    ];

    for (final seg in segments) {
      final paint = Paint()
        ..color = seg.color
        ..style = PaintingStyle.fill;

      final path = Path()
        ..moveTo(cx, cy)
        ..arcTo(
          Rect.fromCircle(center: Offset(cx, cy), radius: r),
          _deg(seg.start),
          _deg(seg.sweep),
          false,
        )
        ..close();
      canvas.drawPath(path, paint);
    }

    // White inner circle to create the "ring" look
    canvas.drawCircle(
      Offset(cx, cy),
      r * 0.6,
      Paint()..color = Colors.white,
    );

    // The horizontal bar of "G"
    final barPaint = Paint()
      ..color = const Color(0xFF4285F4)
      ..style = PaintingStyle.fill;
    canvas.drawRect(
      Rect.fromLTRB(cx, cy - r * 0.12, cx + r, cy + r * 0.12),
      barPaint,
    );

    // Redraw inner circle to clean up bar overflow
    canvas.drawCircle(
      Offset(cx, cy),
      r * 0.55,
      Paint()..color = Colors.white,
    );
  }

  double _deg(double degrees) => degrees * (3.14159265 / 180);

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

class _Segment {
  const _Segment(this.start, double end, this.color) : sweep = end - start;
  final double start;
  final double sweep;
  final Color color;
}
