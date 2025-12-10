# Test Financial Connections

Learn how to test your integration with simulated Financial Connections accounts.

## Get started using a sandbox

Refer to the [testing](https://docs.stripe.com/testing.md) documentation to learn more about testing your Stripe integration.

To test Financial Connections, follow the relevant [use case guide](https://docs.stripe.com/financial-connections/use-cases.md) using a *sandbox* (A sandbox is an isolated test environment that allows you to test Stripe functionality in your account without affecting your live integration. Use sandboxes to safely experiment with new features and changes) and its associated test API keys. Accounts and customers that you make in a sandbox are invisible to your live mode integration.

> The Financial Connections [authentication flow](https://docs.stripe.com/financial-connections/fundamentals.md#authentication-flow) is subject to change, so we don’t recommend automated client-side testing. Usage of Stripe’s API with sandbox test keys is also strictly [rate limited](https://docs.stripe.com/testing.md#rate-limits), which you must account for in your tests.

## How to use test accounts and institutions [Server-side]

When you provide [Stripe.js](https://docs.stripe.com/js.md) with a Financial Connections Session token created using sandbox test keys, the [authentication flow](https://docs.stripe.com/financial-connections/fundamentals.md#authentication-flow) exclusively shows a selection of test institutions managed by Stripe. The client can link accounts from any of these institutions without providing credentials.

Features like [balances](https://docs.stripe.com/financial-connections/balances.md), [account ownership](https://docs.stripe.com/financial-connections/ownership.md), and [transactions](https://docs.stripe.com/financial-connections/transactions.md) work the same way as they do in live mode, except they return testing data instead of real account data.

Sandbox *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) are separate from live webhooks. Learn about [testing your webhook integrations](https://docs.stripe.com/webhooks.md#test-webhook).

## Testing different user authentication scenarios [Client-side]

Stripe provides a set of test institutions exercising different success and failure scenarios, each represented as a bank in the list of featured institutions.

#### Simulating successful authentication

- **Test Institution**: Simulates the user successfully logging into their institution and contains a basic set of test accounts.
- **Test OAuth Institution**: Contains the same test accounts as Test Institution, but instead of authenticating directly with the modal, it opens an OAuth popup for authentication.
- **Ownership Accounts**: Contains test accounts representing different ownership states.
- **Sandbox Bank (OAuth)**: Provides a test institution OAuth popup that allows you to select accounts to link. Sandbox Bank is the most representative of account linking for the majority of live mode institutions.
- **Sandbox Bank (Non-OAuth)**: Provides a Stripe-hosted login form to simulate institutions that don’t support OAuth. Use the following test credentials to proceed:
  - The initial prompt asks for username and password. Entering any input value simulates a successful login.
  - In the password field or any subsequent field, enter `options` (selection from a list), `mfa` (one-time passcode entry), `confirm_mfa` (one-time passcode confirmation), or `security_question` (secret answer entry) to exercise further login prompts.
  - Entering `error` in any field ends the login session; `incorrect` gives you a chance to try again.
- **Invalid Payment Accounts**: Contains test accounts that are unusable for ACH payments.

#### Simulating failed authentication

- **Down Bank (Scheduled)**: The institution’s login API is unavailable for a known time period that the institution communicated to Stripe.
- **Down Bank (Unscheduled)**: The institution’s login API is unavailable without any information about the downtime communicated to Stripe.
- **Down Bank (Error)**: Stripe is experiencing an unknown error communicating with the institution.

> We recommend manually testing OAuth and non-OAuth institutions to make sure that both UI flows work within the context your application. See [additional documentation](https://docs.stripe.com/financial-connections/fundamentals.md#how-stripe-links-financial-accounts) about the differences between OAuth and non-OAuth connections.
