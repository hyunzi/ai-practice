# Use Financial Accounts for platforms and Issuing to set up financial accounts and cards

Follow a sample Financial Accounts for platforms and Issuing integration that sets up a financial account and creates cards.

Homebox is a fictitious vertical SaaS that builds software for home-services companies like HVAC technicians, cleaners, and plumbers. Homebox begins its Financial Accounts for platforms integration by setting up a financial account and creating payment cards. To see how Homebox moves money to and from external bank accounts, see the [Using Financial Accounts for platforms to move money](https://docs.stripe.com/financial-accounts/connect/examples/moving-money.md) example integration.

## Platform onboarding

Homebox is already a Stripe platform with [Payments](https://docs.stripe.com/payments.md) and [Connect](https://docs.stripe.com/connect.md) enabled. Homebox uses [Custom connected accounts](https://docs.stripe.com/connect/accounts.md), and those connected accounts already have the `card_payments` capability enabled.

## Add capabilities

> #### Accounts v2 API compatibility
> 
> The Accounts v2 API doesn’t support Financial Accounts workflows. If you have accounts created with Accounts v2, you can use Accounts v1 to manage the `treasury` and `card_issuing` capabilities.

To use Financial Accounts for platforms and Issuing services, Homebox needs to request the additional `treasury` and `card_issuing` capabilities for the platform’s connected accounts. Each connected account must then onboard before Stripe can create a financial account for it.

To use ACH transfers with Financial Accounts for platforms, Homebox also needs to request the `us_bank_account_ach_payments` capability.

To request the `treasury`, `card_issuing`, and `us_bank_account_ach_payments` capabilities, Homebox makes a request to the [Accounts API](https://docs.stripe.com/api/accounts.md).

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "capabilities[treasury][requested]"=true \
  -d "capabilities[card_issuing][requested]"=true \
  -d "capabilities[us_bank_account_ach_payments][requested]"=true
```

```cli
stripe accounts update {{CONNECTED_ACCOUNT_ID}} \
  -d "capabilities[treasury][requested]"=true \
  -d "capabilities[card_issuing][requested]"=true \
  -d "capabilities[us_bank_account_ach_payments][requested]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    capabilities: {
      treasury: {requested: true},
      card_issuing: {requested: true},
      us_bank_account_ach_payments: {requested: true},
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.update(
  "{{CONNECTED_ACCOUNT_ID}}",
  {
    "capabilities": {
      "treasury": {"requested": True},
      "card_issuing": {"requested": True},
      "us_bank_account_ach_payments": {"requested": True},
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->update(
  '{{CONNECTED_ACCOUNT_ID}}',
  [
    'capabilities' => [
      'treasury' => ['requested' => true],
      'card_issuing' => ['requested' => true],
      'us_bank_account_ach_payments' => ['requested' => true],
    ],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountUpdateParams params =
  AccountUpdateParams.builder()
    .setCapabilities(
      AccountUpdateParams.Capabilities.builder()
        .setCardIssuing(
          AccountUpdateParams.Capabilities.CardIssuing.builder()
            .setRequested(true)
            .build()
        )
        .setUsBankAccountAchPayments(
          AccountUpdateParams.Capabilities.UsBankAccountAchPayments.builder()
            .setRequested(true)
            .build()
        )
        .build()
    )
    .putExtraParam("capabilities[treasury][requested]", true)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().update("{{CONNECTED_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    capabilities: {
      treasury: {
        requested: true,
      },
      card_issuing: {
        requested: true,
      },
      us_bank_account_ach_payments: {
        requested: true,
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountUpdateParams{
  Capabilities: &stripe.AccountUpdateCapabilitiesParams{
    CardIssuing: &stripe.AccountUpdateCapabilitiesCardIssuingParams{
      Requested: stripe.Bool(true),
    },
    USBankAccountACHPayments: &stripe.AccountUpdateCapabilitiesUSBankAccountACHPaymentsParams{
      Requested: stripe.Bool(true),
    },
  },
}
params.AddExtra("capabilities[treasury][requested]", true)
result, err := sc.V1Accounts.Update(context.TODO(), "{{CONNECTED_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Capabilities = new AccountCapabilitiesOptions
    {
        CardIssuing = new AccountCapabilitiesCardIssuingOptions { Requested = true },
        UsBankAccountAchPayments = new AccountCapabilitiesUsBankAccountAchPaymentsOptions
        {
            Requested = true,
        },
    },
};
options.AddExtraParam("capabilities[treasury][requested]", true);
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_ACCOUNT_ID}}", options);
```

To use Hosted Onboarding, Homebox makes a call to [Account Links](https://docs.stripe.com/api/account_links.md) to retrieve a URL that their connected account can use to submit onboarding information for the financial account.

```curl
curl https://api.stripe.com/v1/account_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d account="{{CONNECTEDACCOUNT_ID}}" \
  --data-urlencode refresh_url="https://example.com/reauth" \
  --data-urlencode return_url="https://example.com/return" \
  -d type=account_onboarding
```

```cli
stripe account_links create  \
  --account="{{CONNECTEDACCOUNT_ID}}" \
  --refresh-url="https://example.com/reauth" \
  --return-url="https://example.com/return" \
  --type=account_onboarding
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account_link = client.v1.account_links.create({
  account: '{{CONNECTEDACCOUNT_ID}}',
  refresh_url: 'https://example.com/reauth',
  return_url: 'https://example.com/return',
  type: 'account_onboarding',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account_link = client.v1.account_links.create({
  "account": "{{CONNECTEDACCOUNT_ID}}",
  "refresh_url": "https://example.com/reauth",
  "return_url": "https://example.com/return",
  "type": "account_onboarding",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$accountLink = $stripe->accountLinks->create([
  'account' => '{{CONNECTEDACCOUNT_ID}}',
  'refresh_url' => 'https://example.com/reauth',
  'return_url' => 'https://example.com/return',
  'type' => 'account_onboarding',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountLinkCreateParams params =
  AccountLinkCreateParams.builder()
    .setAccount("{{CONNECTEDACCOUNT_ID}}")
    .setRefreshUrl("https://example.com/reauth")
    .setReturnUrl("https://example.com/return")
    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
AccountLink accountLink = client.v1().accountLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const accountLink = await stripe.accountLinks.create({
  account: '{{CONNECTEDACCOUNT_ID}}',
  refresh_url: 'https://example.com/reauth',
  return_url: 'https://example.com/return',
  type: 'account_onboarding',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountLinkCreateParams{
  Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  RefreshURL: stripe.String("https://example.com/reauth"),
  ReturnURL: stripe.String("https://example.com/return"),
  Type: stripe.String("account_onboarding"),
}
result, err := sc.V1AccountLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountLinkCreateOptions
{
    Account = "{{CONNECTEDACCOUNT_ID}}",
    RefreshUrl = "https://example.com/reauth",
    ReturnUrl = "https://example.com/return",
    Type = "account_onboarding",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.AccountLinks;
AccountLink accountLink = service.Create(options);
```

The response includes a URL the connected account uses to access the application, which must be done before the link expires.

```json
{
  "object": "account_link",
  "created": 1612927106,
  "expires_at": 1612927406,"url": "https://connect.stripe.com/setup/s/iCtLfmYb2tEU"
}
```

Homebox listens for the `account.updated` webhook to confirm the following fields and capabilities on the connected account:

```json
{
  "object": {
    "id": "{{CONNECTED_ACCOUNT_ID}}",
    "object": "account",
    "capabilities": {
      "card_payments": "active",
      "treasury": "active",
      "card_issuing": "active", // Only appears if requesting the `card_issuing` capability.
      "us_bank_account_ach_payments": "active", // Only appears if requesting the `us_bank_account_ach_payments` capability.
    },
    ...
  }
}
```

## Create a FinancialAccount

After Stripe adds the `treasury` capability to an account, Homebox can create the `FinancialAccount` object for the account. To do so, Homebox calls the [Financial Accounts API](https://docs.stripe.com/api/treasury/financial_accounts.md) and requests the `Features` the company wants to provide.

```curl
curl https://api.stripe.com/v1/treasury/financial_accounts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "supported_currencies[]"=usd \
  -d "features[card_issuing][requested]"=true \
  -d "features[deposit_insurance][requested]"=true \
  -d "features[financial_addresses][aba][requested]"=true \
  -d "features[inbound_transfers][ach][requested]"=true \
  -d "features[intra_stripe_flows][requested]"=true \
  -d "features[outbound_payments][ach][requested]"=true \
  -d "features[outbound_payments][us_domestic_wire][requested]"=true \
  -d "features[outbound_transfers][ach][requested]"=true \
  -d "features[outbound_transfers][us_domestic_wire][requested]"=true
```

```cli
stripe treasury financial_accounts create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "supported_currencies[0]"=usd \
  -d "features[card_issuing][requested]"=true \
  -d "features[deposit_insurance][requested]"=true \
  -d "features[financial_addresses][aba][requested]"=true \
  -d "features[inbound_transfers][ach][requested]"=true \
  -d "features[intra_stripe_flows][requested]"=true \
  -d "features[outbound_payments][ach][requested]"=true \
  -d "features[outbound_payments][us_domestic_wire][requested]"=true \
  -d "features[outbound_transfers][ach][requested]"=true \
  -d "features[outbound_transfers][us_domestic_wire][requested]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

financial_account = client.v1.treasury.financial_accounts.create(
  {
    supported_currencies: ['usd'],
    features: {
      card_issuing: {requested: true},
      deposit_insurance: {requested: true},
      financial_addresses: {aba: {requested: true}},
      inbound_transfers: {ach: {requested: true}},
      intra_stripe_flows: {requested: true},
      outbound_payments: {
        ach: {requested: true},
        us_domestic_wire: {requested: true},
      },
      outbound_transfers: {
        ach: {requested: true},
        us_domestic_wire: {requested: true},
      },
    },
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
financial_account = client.v1.treasury.financial_accounts.create(
  {
    "supported_currencies": ["usd"],
    "features": {
      "card_issuing": {"requested": True},
      "deposit_insurance": {"requested": True},
      "financial_addresses": {"aba": {"requested": True}},
      "inbound_transfers": {"ach": {"requested": True}},
      "intra_stripe_flows": {"requested": True},
      "outbound_payments": {
        "ach": {"requested": True},
        "us_domestic_wire": {"requested": True},
      },
      "outbound_transfers": {
        "ach": {"requested": True},
        "us_domestic_wire": {"requested": True},
      },
    },
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$financialAccount = $stripe->treasury->financialAccounts->create(
  [
    'supported_currencies' => ['usd'],
    'features' => [
      'card_issuing' => ['requested' => true],
      'deposit_insurance' => ['requested' => true],
      'financial_addresses' => ['aba' => ['requested' => true]],
      'inbound_transfers' => ['ach' => ['requested' => true]],
      'intra_stripe_flows' => ['requested' => true],
      'outbound_payments' => [
        'ach' => ['requested' => true],
        'us_domestic_wire' => ['requested' => true],
      ],
      'outbound_transfers' => [
        'ach' => ['requested' => true],
        'us_domestic_wire' => ['requested' => true],
      ],
    ],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

FinancialAccountCreateParams params =
  FinancialAccountCreateParams.builder()
    .addSupportedCurrency("usd")
    .setFeatures(
      FinancialAccountCreateParams.Features.builder()
        .setDepositInsurance(
          FinancialAccountCreateParams.Features.DepositInsurance.builder()
            .setRequested(true)
            .build()
        )
        .setFinancialAddresses(
          FinancialAccountCreateParams.Features.FinancialAddresses.builder()
            .setAba(
              FinancialAccountCreateParams.Features.FinancialAddresses.Aba.builder()
                .setRequested(true)
                .build()
            )
            .build()
        )
        .setInboundTransfers(
          FinancialAccountCreateParams.Features.InboundTransfers.builder()
            .setAch(
              FinancialAccountCreateParams.Features.InboundTransfers.Ach.builder()
                .setRequested(true)
                .build()
            )
            .build()
        )
        .setIntraStripeFlows(
          FinancialAccountCreateParams.Features.IntraStripeFlows.builder()
            .setRequested(true)
            .build()
        )
        .setOutboundPayments(
          FinancialAccountCreateParams.Features.OutboundPayments.builder()
            .setAch(
              FinancialAccountCreateParams.Features.OutboundPayments.Ach.builder()
                .setRequested(true)
                .build()
            )
            .setUsDomesticWire(
              FinancialAccountCreateParams.Features.OutboundPayments.UsDomesticWire.builder()
                .setRequested(true)
                .build()
            )
            .build()
        )
        .setOutboundTransfers(
          FinancialAccountCreateParams.Features.OutboundTransfers.builder()
            .setAch(
              FinancialAccountCreateParams.Features.OutboundTransfers.Ach.builder()
                .setRequested(true)
                .build()
            )
            .setUsDomesticWire(
              FinancialAccountCreateParams.Features.OutboundTransfers.UsDomesticWire.builder()
                .setRequested(true)
                .build()
            )
            .build()
        )
        .build()
    )
    .putExtraParam("features[card_issuing][requested]", true)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

FinancialAccount financialAccount =
  client.v1().treasury().financialAccounts().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const financialAccount = await stripe.treasury.financialAccounts.create(
  {
    supported_currencies: ['usd'],
    features: {
      card_issuing: {
        requested: true,
      },
      deposit_insurance: {
        requested: true,
      },
      financial_addresses: {
        aba: {
          requested: true,
        },
      },
      inbound_transfers: {
        ach: {
          requested: true,
        },
      },
      intra_stripe_flows: {
        requested: true,
      },
      outbound_payments: {
        ach: {
          requested: true,
        },
        us_domestic_wire: {
          requested: true,
        },
      },
      outbound_transfers: {
        ach: {
          requested: true,
        },
        us_domestic_wire: {
          requested: true,
        },
      },
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryFinancialAccountCreateParams{
  SupportedCurrencies: []*string{stripe.String("usd")},
  Features: &stripe.TreasuryFinancialAccountCreateFeaturesParams{
    DepositInsurance: &stripe.TreasuryFinancialAccountCreateFeaturesDepositInsuranceParams{
      Requested: stripe.Bool(true),
    },
    FinancialAddresses: &stripe.TreasuryFinancialAccountCreateFeaturesFinancialAddressesParams{
      ABA: &stripe.TreasuryFinancialAccountCreateFeaturesFinancialAddressesABAParams{
        Requested: stripe.Bool(true),
      },
    },
    InboundTransfers: &stripe.TreasuryFinancialAccountCreateFeaturesInboundTransfersParams{
      ACH: &stripe.TreasuryFinancialAccountCreateFeaturesInboundTransfersACHParams{
        Requested: stripe.Bool(true),
      },
    },
    IntraStripeFlows: &stripe.TreasuryFinancialAccountCreateFeaturesIntraStripeFlowsParams{
      Requested: stripe.Bool(true),
    },
    OutboundPayments: &stripe.TreasuryFinancialAccountCreateFeaturesOutboundPaymentsParams{
      ACH: &stripe.TreasuryFinancialAccountCreateFeaturesOutboundPaymentsACHParams{
        Requested: stripe.Bool(true),
      },
      USDomesticWire: &stripe.TreasuryFinancialAccountCreateFeaturesOutboundPaymentsUSDomesticWireParams{
        Requested: stripe.Bool(true),
      },
    },
    OutboundTransfers: &stripe.TreasuryFinancialAccountCreateFeaturesOutboundTransfersParams{
      ACH: &stripe.TreasuryFinancialAccountCreateFeaturesOutboundTransfersACHParams{
        Requested: stripe.Bool(true),
      },
      USDomesticWire: &stripe.TreasuryFinancialAccountCreateFeaturesOutboundTransfersUSDomesticWireParams{
        Requested: stripe.Bool(true),
      },
    },
  },
}
params.AddExtra("features[card_issuing][requested]", true)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryFinancialAccounts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.FinancialAccountCreateOptions
{
    SupportedCurrencies = new List<string> { "usd" },
    Features = new Stripe.Treasury.FinancialAccountFeaturesOptions
    {
        DepositInsurance = new Stripe.Treasury.FinancialAccountFeaturesDepositInsuranceOptions
        {
            Requested = true,
        },
        FinancialAddresses = new Stripe.Treasury.FinancialAccountFeaturesFinancialAddressesOptions
        {
            Aba = new Stripe.Treasury.FinancialAccountFeaturesFinancialAddressesAbaOptions
            {
                Requested = true,
            },
        },
        InboundTransfers = new Stripe.Treasury.FinancialAccountFeaturesInboundTransfersOptions
        {
            Ach = new Stripe.Treasury.FinancialAccountFeaturesInboundTransfersAchOptions
            {
                Requested = true,
            },
        },
        IntraStripeFlows = new Stripe.Treasury.FinancialAccountFeaturesIntraStripeFlowsOptions
        {
            Requested = true,
        },
        OutboundPayments = new Stripe.Treasury.FinancialAccountFeaturesOutboundPaymentsOptions
        {
            Ach = new Stripe.Treasury.FinancialAccountFeaturesOutboundPaymentsAchOptions
            {
                Requested = true,
            },
            UsDomesticWire = new Stripe.Treasury.FinancialAccountFeaturesOutboundPaymentsUsDomesticWireOptions
            {
                Requested = true,
            },
        },
        OutboundTransfers = new Stripe.Treasury.FinancialAccountFeaturesOutboundTransfersOptions
        {
            Ach = new Stripe.Treasury.FinancialAccountFeaturesOutboundTransfersAchOptions
            {
                Requested = true,
            },
            UsDomesticWire = new Stripe.Treasury.FinancialAccountFeaturesOutboundTransfersUsDomesticWireOptions
            {
                Requested = true,
            },
        },
    },
};
options.AddExtraParam("features[card_issuing][requested]", true);
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.FinancialAccounts;
Stripe.Treasury.FinancialAccount financialAccount = service.Create(
    options,
    requestOptions);
```

The response confirms the account is processing. After processing completes and all relevant features are active, Homebox gets a confirmation from their `treasury.financial_account.features_status_updated` webhook listener.

```json
{
  "object": "treasury.financial_account",
  "created": 1612927106,
  "id": "{{FINANCIAL_ACCOUNT_ID}}",
  "country": "US",
  "supported_currencies": ["usd"],
  "financial_addresses": [ // This field is empty until the "financial_addresses.aba" feature becomes active
    {
      "type": "aba",
      "supported_networks": ["ach", "us_domestic_wire"],
      "aba": {
        "account_number_last4": "7890",
        // Use the expand[] parameter to view the `account_number` field hidden by default
        "account_number": "1234567890",
        "routing_number": "000000001",
        "bank_name": "Bank of Earth"
      }
    }
  ],
  "livemode": true,

  // State machine:
  // open - the account is ready to be used
  // closed - the account is closed
  "status": "open",
  "status_details": {
    // `closed` is null if financial account is not closed
    "closed": {
      // List of one or more reasons why the FinancialAccount was closed:
      // - account_rejected
      // - closed_by_platform
      // - other
      "reasons": [],
    }
  },

  active_features: ["card_issuing"],
  pending_features: ["deposit_insurance", "financial_addresses.aba", "outbound_payments.ach", "us_domestic_wire", "inbound_transfers.ach", "outbound_transfers.ach", "outbound_transfers.us_domestic_wire"],
  restricted_features: [],

  "features": {
    "object": "treasury.financial_account_features",
    "card_issuing": {
      "status": "active",
      "status_details": [],
      "access": "active",
    },
    "deposit_insurance": {
        "requested": true,
        "status": "pending", // Becomes "active" after the financial account is set up
        "status_details": [{"code": "activating", "resolution": nil}],
    },
    "financial_addresses": {
        "aba": {
            "requested": true,
            "status": "pending", // Becomes "active" after the financial account is set up
            "status_details": [{"code": "activating", "resolution": nil}],
        },
    },
    "outbound_payments": {
        "ach": {
            "requested": true,
            "status": "pending", // Becomes "active" after the financial account is set up
            "status_details": [{"code": "activating", "resolution": nil}],
        },
    },
    "us_domestic_wire": {
        "requested": true,
        "status": "pending", // Becomes "active" after the financial account is set up
        "status_details": [{"code": "activating", "resolution": nil}],
    },
    "inbound_transfers": {
        "ach": {
            "requested": true,
            "status": "pending", // Becomes "active" after the financial account is set up
            "status_details": [{"code": "activating", "resolution": nil}],
        },
    },
    "outbound_transfers": {
        "ach": {
            "requested": true,
            "status": "pending", // Becomes "active" after the financial account is set up
            "status_details": [{"code": "activating", "resolution": nil}],
        },
    },
    "outbound_payments": {
        "ach": {
            "requested": true,
            "status": "pending", // Becomes "active" after the financial account is set up
            "status_details": [{"code": "activating", "resolution": nil}],
        },
    },
    "outbound_transfers": {
        "us_domestic_wire": {
            "requested": true,
            "status": "pending", // Becomes "active" once the financial account is set up
            "status_details": [{"code": "activating", "resolution": nil}],
        },
    },
  "platform_restrictions": {
    "inbound_flows": "unrestricted",
    "outbound_flows": "unrestricted"
  },
 "metadata": {},
  ...
}

```

## Create a payment cardholder

Before Homebox can create cards for financial accounts, it needs to create cardholders. The cardholders in this example are plumbers who use Homebox services and own the connected accounts on the platform.

# Dashboard

> This is a Dashboard for when testing-method is without-code. View the full page at https://docs.stripe.com/financial-accounts/connect/examples/financial-accounts?testing-method=without-code.

1. Visit the [Connected accounts](https://dashboard.stripe.com/test/issuing/cards) page in the Dashboard.
1. Select the connected account you want to create a cardholder on to expand its details.
1. Select the **Card issuing** tab.
1. Click the **+** button next to **Cardholders**.
1. Fill in the cardholder’s details and click **Create cardholder**.


# API

> This is a API for when testing-method is with-code. View the full page at https://docs.stripe.com/financial-accounts/connect/examples/financial-accounts?testing-method=with-code.

```curl
curl https://api.stripe.com/v1/issuing/cardholders \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d name="Jenny Bath Remodeling" \
  -d type=company \
  --data-urlencode email="jenny@example.com" \
  --data-urlencode phone_number="+18008675309" \
  -d status=active \
  -d "billing[address][line1]"="1234 Main Street" \
  -d "billing[address][city]"="San Francisco" \
  -d "billing[address][state]"=CA \
  -d "billing[address][postal_code]"=94111 \
  -d "billing[address][country]"=US
```

```cli
stripe issuing cardholders create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --name="Jenny Bath Remodeling" \
  --type=company \
  --email="jenny@example.com" \
  --phone-number="+18008675309" \
  --status=active \
  -d "billing[address][line1]"="1234 Main Street" \
  -d "billing[address][city]"="San Francisco" \
  -d "billing[address][state]"=CA \
  -d "billing[address][postal_code]"=94111 \
  -d "billing[address][country]"=US
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

cardholder = client.v1.issuing.cardholders.create(
  {
    name: 'Jenny Bath Remodeling',
    type: 'company',
    email: 'jenny@example.com',
    phone_number: '+18008675309',
    status: 'active',
    billing: {
      address: {
        line1: '1234 Main Street',
        city: 'San Francisco',
        state: 'CA',
        postal_code: '94111',
        country: 'US',
      },
    },
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
cardholder = client.v1.issuing.cardholders.create(
  {
    "name": "Jenny Bath Remodeling",
    "type": "company",
    "email": "jenny@example.com",
    "phone_number": "+18008675309",
    "status": "active",
    "billing": {
      "address": {
        "line1": "1234 Main Street",
        "city": "San Francisco",
        "state": "CA",
        "postal_code": "94111",
        "country": "US",
      },
    },
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$cardholder = $stripe->issuing->cardholders->create(
  [
    'name' => 'Jenny Bath Remodeling',
    'type' => 'company',
    'email' => 'jenny@example.com',
    'phone_number' => '+18008675309',
    'status' => 'active',
    'billing' => [
      'address' => [
        'line1' => '1234 Main Street',
        'city' => 'San Francisco',
        'state' => 'CA',
        'postal_code' => '94111',
        'country' => 'US',
      ],
    ],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardholderCreateParams params =
  CardholderCreateParams.builder()
    .setName("Jenny Bath Remodeling")
    .setType(CardholderCreateParams.Type.COMPANY)
    .setEmail("jenny@example.com")
    .setPhoneNumber("+18008675309")
    .setStatus(CardholderCreateParams.Status.ACTIVE)
    .setBilling(
      CardholderCreateParams.Billing.builder()
        .setAddress(
          CardholderCreateParams.Billing.Address.builder()
            .setLine1("1234 Main Street")
            .setCity("San Francisco")
            .setState("CA")
            .setPostalCode("94111")
            .setCountry("US")
            .build()
        )
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Cardholder cardholder =
  client.v1().issuing().cardholders().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const cardholder = await stripe.issuing.cardholders.create(
  {
    name: 'Jenny Bath Remodeling',
    type: 'company',
    email: 'jenny@example.com',
    phone_number: '+18008675309',
    status: 'active',
    billing: {
      address: {
        line1: '1234 Main Street',
        city: 'San Francisco',
        state: 'CA',
        postal_code: '94111',
        country: 'US',
      },
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardholderCreateParams{
  Name: stripe.String("Jenny Bath Remodeling"),
  Type: stripe.String(stripe.IssuingCardholderTypeCompany),
  Email: stripe.String("jenny@example.com"),
  PhoneNumber: stripe.String("+18008675309"),
  Status: stripe.String(stripe.IssuingCardholderStatusActive),
  Billing: &stripe.IssuingCardholderCreateBillingParams{
    Address: &stripe.AddressParams{
      Line1: stripe.String("1234 Main Street"),
      City: stripe.String("San Francisco"),
      State: stripe.String("CA"),
      PostalCode: stripe.String("94111"),
      Country: stripe.String("US"),
    },
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1IssuingCardholders.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardholderCreateOptions
{
    Name = "Jenny Bath Remodeling",
    Type = "company",
    Email = "jenny@example.com",
    PhoneNumber = "+18008675309",
    Status = "active",
    Billing = new Stripe.Issuing.CardholderBillingOptions
    {
        Address = new AddressOptions
        {
            Line1 = "1234 Main Street",
            City = "San Francisco",
            State = "CA",
            PostalCode = "94111",
            Country = "US",
        },
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cardholders;
Stripe.Issuing.Cardholder cardholder = service.Create(options, requestOptions);
```

The response confirms the cardholder is created.

```json
{
    "id": "{{CARDHOLDER_ID}}",
    "object": "issuing.cardholder",
    "billing": {
        "address": {
            "city": "\"San Francisco\"",
            "country": "US",
            "line1": "\"1234 Main Street\"",
            "postal_code": "94111",
            "state": "CA"
        }
    },
    "created": 1623803705,
    "email": "jenny@example.com",
    "livemode": false,
    "metadata": {},
    "name": "Jenny Bath Remodeling",
    "phone_number": "+18008675309",
    "requirements": {
        "disabled_reason": "under_review",
        "past_due": []
    },
    "spending_controls": {
        "allowed_categories": [],
        "blocked_categories": [],
        "spending_limits": [],
    },
    "status": "active",
    "type": "company"
}
```


## Create payment cards

Now that the connected account has a `FinancialAccount` object associated with it and an available cardholder, Homebox can create a payment card using the `FinancialAccount` balance as the card’s available balance.

# Dashboard

> This is a Dashboard for when testing-method is without-code. View the full page at https://docs.stripe.com/financial-accounts/connect/examples/financial-accounts?testing-method=without-code.

> You can’t create [Financial accounts](https://docs.stripe.com/financial-accounts/connect/account-management/financial-accounts.md) in the Dashboard. You must use the API to create them.

1. Visit the [Connected accounts](https://dashboard.stripe.com/test/issuing/cards) page in the Dashboard.
1. Select the connected account you want to create a cardholder on to expand its details.
1. Select the **Card issuing** tab.
1. Click the **+** button next to **Cards**.
1. Select the card type and the financial account that you want to fund the card with and click **Create**.


# API

> This is a API for when testing-method is with-code. View the full page at https://docs.stripe.com/financial-accounts/connect/examples/financial-accounts?testing-method=with-code.

```curl
curl https://api.stripe.com/v1/issuing/cards \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d currency=usd \
  -d type=virtual \
  -d cardholder="{{ISSUINGCARDHOLDER_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}"
```

```cli
stripe issuing cards create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --currency=usd \
  --type=virtual \
  --cardholder="{{ISSUINGCARDHOLDER_ID}}" \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.create(
  {
    currency: 'usd',
    type: 'virtual',
    cardholder: '{{ISSUINGCARDHOLDER_ID}}',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.create(
  {
    "currency": "usd",
    "type": "virtual",
    "cardholder": "{{ISSUINGCARDHOLDER_ID}}",
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->create(
  [
    'currency' => 'usd',
    'type' => 'virtual',
    'cardholder' => '{{ISSUINGCARDHOLDER_ID}}',
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardCreateParams params =
  CardCreateParams.builder()
    .setCurrency("usd")
    .setType(CardCreateParams.Type.VIRTUAL)
    .setCardholder("{{ISSUINGCARDHOLDER_ID}}")
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Card card = client.v1().issuing().cards().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.create(
  {
    currency: 'usd',
    type: 'virtual',
    cardholder: '{{ISSUINGCARDHOLDER_ID}}',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  Type: stripe.String(stripe.IssuingCardTypeVirtual),
  Cardholder: stripe.String("{{ISSUINGCARDHOLDER_ID}}"),
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1IssuingCards.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardCreateOptions
{
    Currency = "usd",
    Type = "virtual",
    Cardholder = "{{ISSUINGCARDHOLDER_ID}}",
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Create(options, requestOptions);
```

The response confirms the card is issued.

```json
{
  "id": "{{CARD_ID}}",
  "object": "issuing.card",
  "cardholder": {
    "id": "{{CARDHOLDER_ID}}",
    "object": "issuing.cardholder",
    "billing": {
      "address": {
        "city": "San Francisco",
        "country": "US",
        "line1": "123 Main Street",
        "line2": null,
        "postal_code": "94111",
        "state": "CA"
      }
    },
    ...
  },
  "created": 1643293629,
  "currency": "usd",
  "exp_month": 12,
  "exp_year": 2024,
  "last4": "0930",
  "livemode": false,
  ...
}

```


## See also

- [Using Financial Accounts for platforms to move money](https://docs.stripe.com/financial-accounts/connect/examples/moving-money.md)
- [API reference](https://docs.stripe.com/api/treasury/financial_accounts.md)
