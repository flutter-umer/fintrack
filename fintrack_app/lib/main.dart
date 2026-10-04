import 'package:fintrack_app/firebase_options.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'theme/app_theme.dart';
import 'services/auth_service.dart';
import 'screens/splash_screen.dart';
import 'screens/login_screen.dart';
import 'screens/home_screen.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  SystemChrome.setSystemUIOverlayStyle(
    const SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.dark,
      systemNavigationBarColor: AppColors.background,
      systemNavigationBarIconBrightness: Brightness.dark,
    ),
  );

  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  runApp(const FinTrackApp());
}

class FinTrackApp extends StatelessWidget {
  const FinTrackApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'FinTrack',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      // Use the splash screen as the first route so users see the
      // branded splash before auth state resolves.
      home: const _AuthGate(),
    );
  }
}

/// Listens to Firebase auth state and routes accordingly.
///
//
/// Flow:
///   - Shows [SplashScreen] while waiting for the very first auth event.
///   - Once determined: authenticated → [HomeScreen], else → [LoginScreen].
class _AuthGate extends StatefulWidget {
  const _AuthGate();

  @override
  State<_AuthGate> createState() => _AuthGateState();
}

class _AuthGateState extends State<_AuthGate> {
  // Whether we are waiting for Firebase to emit the first auth event.
  bool _resolving = true;
  User? _user;

  @override
  void initState() {
    super.initState();
    // Listen once: after first event resolve the gate.
    AuthService.instance.authStateChanges.listen((user) {
      if (!mounted) return;
      setState(() {
        _user = user;
        _resolving = false;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    // Show branded splash while Firebase resolves auth state.
    if (_resolving) return const SplashScreen();

    if (_user != null) {
      return HomeScreen(user: _user!);
    }

    return const LoginScreen();
  }
}
