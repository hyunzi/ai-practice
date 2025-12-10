# Platforms and marketplaces with Stripe Connect

Build a SaaS platform or marketplace with Connect.

## Get started

Use Connect to build a platform, marketplace, or other business that manages payments and moves money between multiple parties.
[Design your Connect integration](https://docs.stripe.com/connect/design-an-integration.md)
## Build a SaaS platform or marketplace 

[Connect and the Accounts v2 API](https://docs.stripe.com/connect/accounts-v2.md): Create a unified identity to represent each of your platform?�s connected accounts with one or more configurations, such as merchant or customer.

[Build a SaaS platform](https://docs.stripe.com/connect/saas.md): Provide platform services to businesses that collect payments from their own customers.

[Build a marketplace](https://docs.stripe.com/connect/marketplace.md): Collect payments from customers and automatically pay out a portion to sellers or service providers on your marketplace.

## Connected account management 

[Choose your onboarding configuration](https://docs.stripe.com/connect/onboarding.md): Learn about the different options for onboarding your connected accounts.

[Enable account capabilities](https://docs.stripe.com/connect/account-capabilities.md): Enable capabilities for your accounts.

[Collect verification information](https://docs.stripe.com/connect/required-verification-information.md): Learn what information you need to collect and verify for each account.

## Payment processing 

[Create a charge](https://docs.stripe.com/connect/charges.md): Create a charge and split payments between your platform and your sellers or service providers.

[Work with Connect account balances](https://docs.stripe.com/connect/account-balances.md): Learn about platform and connected account balances.

[Pay out to connected accounts](https://docs.stripe.com/connect/payouts-connected-accounts.md): Manage payouts and external accounts for your connected accounts.

## Platform administration 

[Use the platform pricing tool](https://docs.stripe.com/connect/platform-pricing-tools.md): Set platform processing fees for your connected accounts.

[Manage connected accounts with the Dashboard](https://docs.stripe.com/connect/dashboard.md): Review and take action on your connected accounts.

## More resources 

[Use Stripe Tax with Connect](https://docs.stripe.com/tax/connect.md): Calculate, collect, and report taxes for your platform or connected accounts.

[Use Stripe Radar with Connect](https://docs.stripe.com/connect/radar.md): Learn how to use Stripe Radar to identify fraud in Connect account charges.

[Use Connect embedded components](https://docs.stripe.com/connect/get-started-connect-embedded-components.md): Learn how to add connected account dashboard functionality to your website and mobile applications.

|                                 | Next-day settlement                                                                                                                     | Instant Payouts                                                                          |
| ------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
| **Best for**                    | Continuous, automatic liquidity management                                                                                              | As-needed, manual liquidity management                                                   |
| **Eligible funds**              | Settled USD earnings excluding ACH direct debits                                                                                        | All pending earnings (up to your Instant Payouts limit) after a card charge is completed |
| **When funds become available** | Next business day                                                                                                                       | Within 30 minutes                                                                        |
| **How to activate**             | Choose on or off in your [payout settings](https://dashboard.stripe.com/settings/payouts) to automatically apply to all eligible funds. | Manually request for each payout in your Balances Dashboard                              |
| **Fee**                         | 0.6%                                                                                                                                    | [See pricing](https://docs.stripe.com/payouts/instant-payouts.md#pricing)                |
