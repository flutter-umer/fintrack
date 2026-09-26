import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';

/// Result wrapper so the UI never touches FirebaseAuthException directly.
class AuthResult {
  const AuthResult.success(this.user) : errorMessage = null;
  const AuthResult.failure(this.errorMessage) : user = null;

  final User? user;
  final String? errorMessage;

  bool get isSuccess => errorMessage == null;
}

/// Maps raw Firebase error codes to user-friendly messages.
String _mapFirebaseError(FirebaseAuthException e) {
  switch (e.code) {
    case 'email-already-in-use':
      return 'An account already exists with this email.';
    case 'invalid-email':
      return 'Please enter a valid email address.';
    case 'weak-password':
      return 'Your password is too weak. Use at least 6 characters.';
    case 'user-not-found':
      return 'No account found with this email.';
    case 'wrong-password':
      return 'Incorrect password. Please try again.';
    case 'invalid-credential':
      return 'The email or password is incorrect.';
    case 'network-request-failed':
      return 'Please check your internet connection.';
    case 'account-exists-with-different-credential':
      return 'An account already exists using a different sign-in method.';
    case 'user-disabled':
      return 'This account has been disabled. Contact support.';
    case 'too-many-requests':
      return 'Too many attempts. Please try again later.';
    case 'operation-not-allowed':
      return 'This sign-in method is not enabled.';
    default:
      return 'An unexpected error occurred. Please try again.';
  }
}

/// Central authentication service for FinTrack.
///
/// All Firebase-specific code is isolated here. UI screens only interact
/// with [AuthResult] and the stream/getter surface below.
class AuthService {
  AuthService._();
  static final AuthService instance = AuthService._();

  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn();

  // ──────────────────────────────────────────────────
  // State
  // ──────────────────────────────────────────────────

  /// Stream that emits whenever the auth state changes (login / logout).
  Stream<User?> get authStateChanges => _auth.authStateChanges();

  /// The currently signed-in Firebase user, or null.
  User? get currentUser => _auth.currentUser;

  // ──────────────────────────────────────────────────
  // Email / Password
  // ──────────────────────────────────────────────────

  /// Creates a new Firebase account with [name], [email], and [password].
  /// After creation, the display name is set via [updateDisplayName].
  Future<AuthResult> signUpWithEmail({
    required String name,
    required String email,
    required String password,
  }) async {
    try {
      final credential = await _auth.createUserWithEmailAndPassword(
        email: email.trim(),
        password: password,
      );
      // Persist the display name immediately after account creation.
      await credential.user?.updateDisplayName(name.trim());
      await credential.user?.reload();
      return AuthResult.success(_auth.currentUser);
    } on FirebaseAuthException catch (e) {
      return AuthResult.failure(_mapFirebaseError(e));
    } catch (_) {
      return AuthResult.failure('An unexpected error occurred. Please try again.');
    }
  }

  /// Signs in with [email] and [password].
  Future<AuthResult> signInWithEmail({
    required String email,
    required String password,
  }) async {
    try {
      final credential = await _auth.signInWithEmailAndPassword(
        email: email.trim(),
        password: password,
      );
      return AuthResult.success(credential.user);
    } on FirebaseAuthException catch (e) {
      return AuthResult.failure(_mapFirebaseError(e));
    } catch (_) {
      return AuthResult.failure('An unexpected error occurred. Please try again.');
    }
  }

  // ──────────────────────────────────────────────────
  // Google
  // ──────────────────────────────────────────────────

  /// Launches the Google Sign-In flow and authenticates with Firebase.
  Future<AuthResult> signInWithGoogle() async {
    try {
      final GoogleSignInAccount? googleUser = await _googleSignIn.signIn();
      if (googleUser == null) {
        // User cancelled the sign-in dialog.
        return const AuthResult.failure('Google sign-in was cancelled.');
      }
      final GoogleSignInAuthentication googleAuth =
          await googleUser.authentication;

      final credential = GoogleAuthProvider.credential(
        accessToken: googleAuth.accessToken,
        idToken: googleAuth.idToken,
      );

      final userCredential = await _auth.signInWithCredential(credential);
      return AuthResult.success(userCredential.user);
    } on FirebaseAuthException catch (e) {
      return AuthResult.failure(_mapFirebaseError(e));
    } catch (_) {
      return AuthResult.failure('Google sign-in failed. Please try again.');
    }
  }

  // ──────────────────────────────────────────────────
  // Password Reset
  // ──────────────────────────────────────────────────

  /// Sends a password-reset email to [email].
  Future<AuthResult> sendPasswordResetEmail(String email) async {
    try {
      await _auth.sendPasswordResetEmail(email: email.trim());
      return const AuthResult.success(null);
    } on FirebaseAuthException catch (e) {
      return AuthResult.failure(_mapFirebaseError(e));
    } catch (_) {
      return AuthResult.failure('Failed to send reset email. Please try again.');
    }
  }

  // ──────────────────────────────────────────────────
  // Sign Out
  // ──────────────────────────────────────────────────

  /// Signs out from both Firebase and Google (if Google was used).
  Future<void> signOut() async {
    await Future.wait([
      _auth.signOut(),
      _googleSignIn.signOut(),
    ]);
  }
}
