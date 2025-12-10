# Multi-currency settlement

Accept, settle, and pay out funds in multiple currencies.
Available in: EU, GB, CH, NO, LI, SG, HK, AU, AE, US
If you?�re a Connect platform looking to offer multi-currency settlement capabilities to your connected accounts, see our [Connect Docs](https://docs.stripe.com/connect/multicurrency-settlement.md).

Stripe automatically converts all incoming funds into the default currency of your home country. With multi-currency settlement, you can configure your account to accrue balances and get paid out in additional currencies without incurring foreign exchange fees.

## Enable multi-currency settlement

To configure your account to receive settlement and pay out in multiple currencies, configure the currencies and bank accounts in your [Dashboard](https://dashboard.stripe.com/account/payouts).
![Bank accounts and currencies settings in the Dashboard](https://b.stripecdn.com/docs-statics-srv/assets/bank-accounts-and-currencies-page.cd3a7a8bf6a23667bb5f45f2bf7e19c4.png)

### Configure settlement currencies

In your Payout Settings, click **Manage Currencies** to select the settlement currencies you want to receive funds in. Balances accrue in each currency for payments you accept in that currency.

### Attach bank accounts to receive payouts in local currencies

You must provide a separate supported bank account for each settlement currency you configure to receive payouts in that currency. You must match the currency of the bank account to the settlement currency. After you provide the corresponding bank accounts, Stripe directs payouts from your multi-currency balances to their respective bank accounts in the matching currency.

### Configure payout settings

After you meet a currency?�s [minimum payout amount](https://docs.stripe.com/payouts/multicurrency-settlement.md#multicurrency-settlement-fees), payouts follow your configured [payout schedule](https://docs.stripe.com/connect/manage-payout-schedule.md), whether manual or automatic. You can?�t pay out until your balance for the currency meets the minimum payout amount.

## Fees and minimum payout amounts for multi-currency settlement 
[See table on original page](https://docs.stripe.com/payouts/multicurrency-settlement)
If your business is based in a currently ineligible country for multi-currency settlement, [contact support](https://support.stripe.com) to help us with expansion planning.

## Instant currency conversion for your Stripe balances

You can use instant [currency conversion](https://docs.stripe.com/instant-currency-conversion.md) to convert between currencies in your payments balance or Financial Account.

## See also

- [Supported currencies](https://docs.stripe.com/currencies.md)
- [Localize prices](https://docs.stripe.com/payments/currencies/localize-prices.md)
