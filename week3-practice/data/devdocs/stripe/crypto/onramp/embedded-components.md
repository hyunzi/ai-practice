# Embedded components onramp

Learn about the onramp mobile SDK.

The Embedded components onramp is a native mobile SDK, supporting iOS, Android, and React Native. This integration provides UI customization such as customizing color, button style, and supporting light-mode and dark-mode. It provides the Stripe checkout infrastructure with minimal Stripe branding, allowing you to maintain your app’s existing UI and branding.

## Sign up

Embedded components onramp is in private preview. [Sign up to join the waitlist](https://docs.stripe.com/crypto/onramp.md#sign-up)

## Customer experience

1. A customer visits your app and wants to convert fiat to crypto or purchase crypto.
1. Your app prompts them to onboard by signing up or logging in to [Link](https://docs.stripe.com/payments/link.md).
1. They sign up or log into Link without leaving your app.
1. They give you permissions to use their saved payment methods and wallets. If they’re missing required data (such as KYC information, payment methods, or wallets) they complete account configuration in your app.
1. They successfully purchase or convert crypto in a CryptoOnramp session in your app. Returning customers have the option for one-click checkout.

## Sample apps

Explore our sample apps:

- [iOS](https://github.com/stripe/stripe-ios/tree/master/Example/CryptoOnramp%20Example)
- [Android](https://github.com/stripe/stripe-android/tree/master/crypto-onramp-example)
