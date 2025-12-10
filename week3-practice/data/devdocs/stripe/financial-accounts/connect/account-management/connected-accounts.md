# Working with connected accounts

Request the treasury capability and collect onboarding requirements for your connected accounts.

> #### Accounts v2 API compatibility
> 
> The Accounts v2 API doesn’t support Financial Accounts workflows. If you have accounts created with Accounts v2, you can use Accounts v1 to manage the `treasury` and `card_issuing` capabilities.

To use Financial Accounts for platforms, your platform must have a Stripe *Connect* (Connect is Stripe's solution for multi-party businesses, such as marketplace or software platforms, to route payments between sellers, customers, and other recipients) integration. Stripe Connect enables a platform to provide connected accounts to sellers and service providers. For an overview of how connected accounts fit into the Financial Accounts for platforms account structure, see the [Financial Accounts for platforms accounts structure](https://docs.stripe.com/financial-accounts/connect/account-management/accounts-structure.md) guide.

Financial Accounts for platforms only supports connected accounts that don’t use a Stripe-hosted dashboard and where your platform is responsible for requirements collection and loss liability, including Custom connected accounts. Learn how to [create connected accounts](https://docs.stripe.com/connect/design-an-integration.md?connect-onboarding-surface=api&connect-dashboard-type=none&connect-economic-model=buy-rate&connect-loss-liability-owner=platform&connect-charge-type=direct) that work with Financial Accounts for platforms.

As a platform with connected accounts, you’re responsible for maintaining a minimum API version, communicating terms of service updates to your connected accounts, handling information requests from them, and providing them with support. Because your platform is ultimately responsible for the losses your connected accounts incur, you’re also responsible for vetting them for fraud. To learn more, read the [Financial Accounts for platforms fraud guide](https://docs.stripe.com/financial-accounts/connect/examples/fraud-guide.md).

Connected accounts require specific capabilities enabled on the account to use features of Financial Accounts for platforms. Different features require different capabilities, which might require additional information about your connected account owners. The `treasury` capability, for example, is a requirement on connected accounts for Financial Accounts for platforms access. When you request `treasury` for an account, additional fields become required for that connected account before the account can use Financial Accounts for platforms.

Before you create connected accounts in live mode for your Financial Accounts for platforms integration, we recommend you first create test connected accounts in a *sandbox* (A sandbox is an isolated test environment that allows you to test Stripe functionality in your account without affecting your live integration. Use sandboxes to safely experiment with new features and changes) environment. Test connected accounts can’t receive or send real money and can’t be used in live mode, but are otherwise identical in configuration and functionality.

## Checking current connected account types

If your platform already has a Connect integration with connected accounts but are unsure of their type, you can use the Dashboard or API to retrieve this information.

#### Dashboard

Navigate to the [Connected accounts page](https://dashboard.stripe.com/test/connect/accounts/overview) in the Dashboard. We list your connected accounts in a table format.

To find the account features, select an account in the table to open the detailed view, then click **Profile** > **Account information**.

#### API request

Use `GET /v1/accounts` to retrieve a list of the accounts on your platform.

```json
{
  "object": "list",
  "data": [
    {
      "id": "acct_1KUbgB2E0Hr5XQiY",
      "object": "account",
      "controller": {
        "type": "application",
        "dashboard": {
          "type": "none"
        },
        "losses": {
          "payments": "application"
        },
        "requirement_collection": "application",
        "fees": {
          "payer": "application"
        },
      },
      ...
    }
  ]
}
```

## Create a new connected account with the treasury capability

> This guide demonstrates how to create a new connected account using the Stripe API for Financial Accounts for platforms and isn’t exhaustive. For complete documentation on creating a connected account, including through hosted onboarding, see the [Connect integration guide](https://docs.stripe.com/connect/design-an-integration.md?connect-onboarding-surface=api&connect-dashboard-type=none&connect-economic-model=buy-rate&connect-loss-liability-owner=platform&connect-charge-type=direct).

Use `POST /v1/accounts` to create a new connected account. Request the following capabilities for the account, which are required to use Financial Accounts for platforms:

- `transfers` (required for all connected accounts)
- `treasury`

> You can update the account later to request these capabilities if you don’t do so when creating the account.

If you want to issue cards with Stripe Issuing to your connected account, you must request the `card_issuing` capability, as well. See the [Working with Stripe Issuing cards](https://docs.stripe.com/financial-accounts/connect/account-management/issuing-cards.md) guide for more information.

If you want to use ACH to transfer funds to or from an external account, you must also request the `us_bank_account_ach_payments` capability.

With all the previous options included, the request resembles the following:

```javascript
const account = await stripe.accounts.create({
  country: 'US',
  email: email,
  capabilities: {
    transfers: {requested: true},
    treasury: {requested: true},
    card_issuing: {requested: true},
  },
  controller: {
    dashboard: {type: "none"},
    losses: {payments: "application"},
    requirement_collection: "application",
    fees: {payer: "application"}
  },
});
```

If successful, the response you receive confirms the connected account and requested `capabilities`.

```json
{
  "id": "acct_1234",
  "object": "account",
  "capabilities": {
    "card_issuing": "inactive", // Should be requested only for Stripe Issuing users.
    "treasury": "inactive",
    "us_bank_account_ach_payments": "inactive"
  },
  ...
}
```

To learn more about connected account capabilities, see the [Account capabilities](https://docs.stripe.com/connect/account-capabilities.md) guide for Connect.

## Update a connected account to include the treasury capability

If you already have a connected account with `card_payments` enabled, use `POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}` to update the account with the associated ID with a request for the `treasury` capability. The following request updates a connected account with a request for the `treasury` capability, and includes the optional capabilities of `card_issuing` and `us_bank_account_ach_payments`:

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTEDACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "capabilities[treasury][requested]"=true \
  -d "capabilities[card_issuing][requested]"=true \
  -d "capabilities[us_bank_account_ach_payments][requested]"=true
```

```cli
stripe accounts update {{CONNECTEDACCOUNT_ID}} \
  -d "capabilities[treasury][requested]"=true \
  -d "capabilities[card_issuing][requested]"=true \
  -d "capabilities[us_bank_account_ach_payments][requested]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTEDACCOUNT_ID}}',
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
  "{{CONNECTEDACCOUNT_ID}}",
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
  '{{CONNECTEDACCOUNT_ID}}',
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
Account account = client.v1().accounts().update("{{CONNECTEDACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTEDACCOUNT_ID}}',
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
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTEDACCOUNT_ID}}", params)
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
Account account = service.Update("{{CONNECTEDACCOUNT_ID}}", options);
```

Use `POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}` to update connected account capabilities for connected accounts that already have a `FinancialAccount` assigned. See [Working with financial accounts](https://docs.stripe.com/financial-accounts/connect/account-management/financial-accounts.md) or the [FinancialAccount object](https://docs.stripe.com/api/treasury/financial_accounts/object.md) API documentation for more information.

## Onboard the connected account

After you create an account, you must onboard the seller or service provider to the account for ownership. The [Account](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-currently_due) object that represents the connected account has a `requirements` hash that contains `currently_due` [identity verification](https://docs.stripe.com/connect/handling-api-verification.md) requirements. The seller or service provider on your platform must provide the details itemized in the `requirements` hash to enable charges and [payouts](https://docs.stripe.com/payouts.md) on their connected account and enable all requested features of their financial account.

You have two options for onboarding connected account owners to Financial Accounts for platforms: [hosted onboarding](https://docs.stripe.com/financial-accounts/connect/account-management/connected-accounts.md#using-hosted-onboarding) and [custom onboarding](https://docs.stripe.com/financial-accounts/connect/account-management/connected-accounts.md#using-custom-onboarding). We recommend hosted onboarding.

If you create a test `Account` object and want to bypass onboarding requirements to test functionality, use `POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}` to [provide test values](https://docs.stripe.com/connect/testing-verification.md) that fulfill all the requirements. The following request uses a previously created connected account to apply the required account details.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "tos_acceptance[date]"=1547923073 \
  -d "tos_acceptance[ip]"="172.18.80.19" \
  -d "settings[treasury][tos_acceptance][date]"=1547923073 \
  -d "settings[treasury][tos_acceptance][ip]"="172.18.80.19" \
  -d "business_profile[mcc]"=5045 \
  --data-urlencode "business_profile[url]"="https://bestcookieco.com" \
  -d "company[address][city]"=Schenectady \
  -d "company[address][line1]"="123 State St" \
  -d "company[address][postal_code]"=12345 \
  -d "company[address][state]"=NY \
  -d "company[tax_id]"=000000000 \
  -d "company[name]"="The Best Cookie Co" \
  -d "company[phone]"=8888675309 \
  -d "individual[first_name]"=Jenny \
  -d "individual[last_name]"=Rosen
```

```cli
stripe accounts update {{CONNECTED_ACCOUNT_ID}} \
  -d "tos_acceptance[date]"=1547923073 \
  -d "tos_acceptance[ip]"="172.18.80.19" \
  -d "settings[treasury][tos_acceptance][date]"=1547923073 \
  -d "settings[treasury][tos_acceptance][ip]"="172.18.80.19" \
  -d "business_profile[mcc]"=5045 \
  -d "business_profile[url]"="https://bestcookieco.com" \
  -d "company[address][city]"=Schenectady \
  -d "company[address][line1]"="123 State St" \
  -d "company[address][postal_code]"=12345 \
  -d "company[address][state]"=NY \
  -d "company[tax_id]"=000000000 \
  -d "company[name]"="The Best Cookie Co" \
  -d "company[phone]"=8888675309 \
  -d "individual[first_name]"=Jenny \
  -d "individual[last_name]"=Rosen
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    tos_acceptance: {
      date: 1547923073,
      ip: '172.18.80.19',
    },
    settings: {
      treasury: {
        tos_acceptance: {
          date: 1547923073,
          ip: '172.18.80.19',
        },
      },
    },
    business_profile: {
      mcc: '5045',
      url: 'https://bestcookieco.com',
    },
    company: {
      address: {
        city: 'Schenectady',
        line1: '123 State St',
        postal_code: '12345',
        state: 'NY',
      },
      tax_id: '000000000',
      name: 'The Best Cookie Co',
      phone: '8888675309',
    },
    individual: {
      first_name: 'Jenny',
      last_name: 'Rosen',
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
    "tos_acceptance": {"date": 1547923073, "ip": "172.18.80.19"},
    "settings": {
      "treasury": {"tos_acceptance": {"date": 1547923073, "ip": "172.18.80.19"}},
    },
    "business_profile": {"mcc": "5045", "url": "https://bestcookieco.com"},
    "company": {
      "address": {
        "city": "Schenectady",
        "line1": "123 State St",
        "postal_code": "12345",
        "state": "NY",
      },
      "tax_id": "000000000",
      "name": "The Best Cookie Co",
      "phone": "8888675309",
    },
    "individual": {"first_name": "Jenny", "last_name": "Rosen"},
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
    'tos_acceptance' => [
      'date' => 1547923073,
      'ip' => '172.18.80.19',
    ],
    'settings' => [
      'treasury' => [
        'tos_acceptance' => [
          'date' => 1547923073,
          'ip' => '172.18.80.19',
        ],
      ],
    ],
    'business_profile' => [
      'mcc' => '5045',
      'url' => 'https://bestcookieco.com',
    ],
    'company' => [
      'address' => [
        'city' => 'Schenectady',
        'line1' => '123 State St',
        'postal_code' => '12345',
        'state' => 'NY',
      ],
      'tax_id' => '000000000',
      'name' => 'The Best Cookie Co',
      'phone' => '8888675309',
    ],
    'individual' => [
      'first_name' => 'Jenny',
      'last_name' => 'Rosen',
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
    .setTosAcceptance(
      AccountUpdateParams.TosAcceptance.builder()
        .setDate(1547923073L)
        .setIp("172.18.80.19")
        .build()
    )
    .setSettings(
      AccountUpdateParams.Settings.builder()
        .setTreasury(
          AccountUpdateParams.Settings.Treasury.builder()
            .setTosAcceptance(
              AccountUpdateParams.Settings.Treasury.TosAcceptance.builder()
                .setDate(1547923073L)
                .setIp("172.18.80.19")
                .build()
            )
            .build()
        )
        .build()
    )
    .setBusinessProfile(
      AccountUpdateParams.BusinessProfile.builder()
        .setMcc("5045")
        .setUrl("https://bestcookieco.com")
        .build()
    )
    .setCompany(
      AccountUpdateParams.Company.builder()
        .setAddress(
          AccountUpdateParams.Company.Address.builder()
            .setCity("Schenectady")
            .setLine1("123 State St")
            .setPostalCode("12345")
            .setState("NY")
            .build()
        )
        .setTaxId("000000000")
        .setName("The Best Cookie Co")
        .setPhone("8888675309")
        .build()
    )
    .setIndividual(
      AccountUpdateParams.Individual.builder()
        .setFirstName("Jenny")
        .setLastName("Rosen")
        .build()
    )
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
    tos_acceptance: {
      date: 1547923073,
      ip: '172.18.80.19',
    },
    settings: {
      treasury: {
        tos_acceptance: {
          date: 1547923073,
          ip: '172.18.80.19',
        },
      },
    },
    business_profile: {
      mcc: '5045',
      url: 'https://bestcookieco.com',
    },
    company: {
      address: {
        city: 'Schenectady',
        line1: '123 State St',
        postal_code: '12345',
        state: 'NY',
      },
      tax_id: '000000000',
      name: 'The Best Cookie Co',
      phone: '8888675309',
    },
    individual: {
      first_name: 'Jenny',
      last_name: 'Rosen',
    },
  }
);
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    TosAcceptance = new AccountTosAcceptanceOptions
    {
        Date = DateTimeOffset.FromUnixTimeSeconds(1547923073).UtcDateTime,
        Ip = "172.18.80.19",
    },
    Settings = new AccountSettingsOptions
    {
        Treasury = new AccountSettingsTreasuryOptions
        {
            TosAcceptance = new AccountSettingsTreasuryTosAcceptanceOptions
            {
                Date = DateTimeOffset.FromUnixTimeSeconds(1547923073).UtcDateTime,
                Ip = "172.18.80.19",
            },
        },
    },
    BusinessProfile = new AccountBusinessProfileOptions
    {
        Mcc = "5045",
        Url = "https://bestcookieco.com",
    },
    Company = new AccountCompanyOptions
    {
        Address = new AddressOptions
        {
            City = "Schenectady",
            Line1 = "123 State St",
            PostalCode = "12345",
            State = "NY",
        },
        TaxId = "000000000",
        Name = "The Best Cookie Co",
        Phone = "8888675309",
    },
    Individual = new AccountIndividualOptions { FirstName = "Jenny", LastName = "Rosen" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_ACCOUNT_ID}}", options);
```

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.AccountParams{
  TOSAcceptance: &stripe.AccountTOSAcceptanceParams{
    Date: stripe.Int64(1547923073),
    IP: stripe.String("172.18.80.19"),
  },
  Settings: &stripe.Settings{
    Treasury: &stripe.Settings.Treasury{
      TOSAcceptance: &stripe.Settings.Treasury.TOSAcceptance{
        Date: stripe.Int64(1547923073),
        IP: stripe.String("172.18.80.19"),
      }
    }
  },
  BusinessProfile: &stripe.AccountBusinessProfileParams{
    MCC: stripe.String("5045"),
    URL: stripe.String("https://bestcookieco.com"),
  },
  Company: &stripe.AccountCompanyParams{
    Address: &stripe.AccountCompanyAddressParams{
      City: stripe.String("Schenectady"),
      Line1: stripe.String("123 State St"),
      PostalCode: stripe.String("12345"),
      State: stripe.String("NY"),
    },
    TaxID: stripe.String("000000000"),
    Name: stripe.String("The Best Cookie Co"),
    Phone: stripe.String("8888675309"),
  },
  Individual: &stripe.AccountIndividualParams{
    FirstName: stripe.String("Jenny"),
    LastName: stripe.String("Rosen"),
  },
};
result, _ := account.Update("{{CONNECTED_ACCOUNT_ID}}", params);
```

### Using hosted onboarding

Use Connect Onboarding to efficiently collect required information. That offloads the verification complexity from your platform to Stripe and collects the terms of the service agreement. Alternatively, you can write your own API requests for initial integration, but must monitor for changes to compliance requirements to keep your onboarding workflow current. Learn how to [create connected accounts](https://docs.stripe.com/connect/design-an-integration.md?connect-onboarding-surface=api&connect-dashboard-type=none&connect-economic-model=buy-rate&connect-loss-liability-owner=platform&connect-charge-type=direct) that work with Financial Accounts for platforms.

Before you can use Connect Onboarding, you must provide the name, color, and icon of your brand in the **Branding** section of your [Connect settings page](https://dashboard.stripe.com/test/settings/connect). Doing so customizes the visual appearance of the form that sellers and service providers interact with when onboarding to your platform.

To take advantage of Connect Onboarding, use `POST /v1/account_links` to create an `AccountLink` to provide to the seller or service provider who’s going to take ownership of the connected account:

> For security, don’t email, text, or otherwise send account link URLs directly to your user. Instead, redirect the authenticated user to the account link URL from within your platform’s application.

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

The response you receive includes the URL to provide to your user.

```json
{
  "object": "account_link",
  "created": 1612927106,
  "expires_at": 1612927406,
  "url": "https://connect.stripe.com/setup/s/iCtLfmYb2tEU"
}
```

### Using embedded onboarding

Embedded onboarding is a themeable onboarding UI with limited Stripe branding. It gives you more UX control than Stripe’s hosted onboarding solution. With embedded onboarding, you get a customized onboarding flow without the complexity and maintenance associated with updating your onboarding integration as compliance requirements change.

Your platform embeds the [Account onboarding component](https://docs.stripe.com/connect/supported-embedded-components/account-onboarding.md) in your application, and your connected accounts interact with the embedded component without leaving your application. Embedded onboarding uses the [Accounts API](https://docs.stripe.com/api/accounts.md) to read the requirements and generate an onboarding form with robust data validation that’s localized for all Stripe-supported countries.

### Using custom onboarding

If you prefer to build custom onboarding for your users, use `POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}` and `POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}/persons/{{PERSON_ID}}` to update the relevant `Account` and `Person` objects with the required information.

You must also confirm that the connected account owner has read and agreed to the [Financial Accounts for platforms Agreement](https://stripe.com/treasury-connect-account/legal). See [Handling verification with the API](https://docs.stripe.com/connect/handling-api-verification.md) for additional details on fulfilling onboarding requirements.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTEDACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "company[name]"=Homebox \
  -d "company[address][line1]"="123 Market St." \
  -d "company[address][city]"="San Francisco" \
  -d "company[address][state]"=CA \
  -d "company[address][postal_code]"=94107 \
  -d "company[address][country]"=US
```

```cli
stripe accounts update {{CONNECTEDACCOUNT_ID}} \
  -d "company[name]"=Homebox \
  -d "company[address][line1]"="123 Market St." \
  -d "company[address][city]"="San Francisco" \
  -d "company[address][state]"=CA \
  -d "company[address][postal_code]"=94107 \
  -d "company[address][country]"=US
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTEDACCOUNT_ID}}',
  {
    company: {
      name: 'Homebox',
      address: {
        line1: '123 Market St.',
        city: 'San Francisco',
        state: 'CA',
        postal_code: '94107',
        country: 'US',
      },
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
  "{{CONNECTEDACCOUNT_ID}}",
  {
    "company": {
      "name": "Homebox",
      "address": {
        "line1": "123 Market St.",
        "city": "San Francisco",
        "state": "CA",
        "postal_code": "94107",
        "country": "US",
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->update(
  '{{CONNECTEDACCOUNT_ID}}',
  [
    'company' => [
      'name' => 'Homebox',
      'address' => [
        'line1' => '123 Market St.',
        'city' => 'San Francisco',
        'state' => 'CA',
        'postal_code' => '94107',
        'country' => 'US',
      ],
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
    .setCompany(
      AccountUpdateParams.Company.builder()
        .setName("Homebox")
        .setAddress(
          AccountUpdateParams.Company.Address.builder()
            .setLine1("123 Market St.")
            .setCity("San Francisco")
            .setState("CA")
            .setPostalCode("94107")
            .setCountry("US")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().update("{{CONNECTEDACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTEDACCOUNT_ID}}',
  {
    company: {
      name: 'Homebox',
      address: {
        line1: '123 Market St.',
        city: 'San Francisco',
        state: 'CA',
        postal_code: '94107',
        country: 'US',
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
  Company: &stripe.AccountUpdateCompanyParams{
    Name: stripe.String("Homebox"),
    Address: &stripe.AccountUpdateCompanyAddressParams{
      Line1: stripe.String("123 Market St."),
      City: stripe.String("San Francisco"),
      State: stripe.String("CA"),
      PostalCode: stripe.String("94107"),
      Country: stripe.String("US"),
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTEDACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Company = new AccountCompanyOptions
    {
        Name = "Homebox",
        Address = new AddressOptions
        {
            Line1 = "123 Market St.",
            City = "San Francisco",
            State = "CA",
            PostalCode = "94107",
            Country = "US",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTEDACCOUNT_ID}}", options);
```

### Requirements

The fields in the following table are required for Financial Accounts for platforms users.

| Entity type                                                          | At onboarding                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Individual, Sole proprietorship                                      | Entity details:
  - Business names (customer facing and legal)
  - Legal entity type
  - Business address
  - Business phone number
  - Product or service description
  - Industry or Merchant category code
  - Tax ID Number (SSN, ITIN, or EIN)
  - Financial Accounts for platforms TOS acceptance
  - Stripe TOS acceptance
Owner details:
  - Legal name
  - Date of birth
  - Email address
  - Residential address
  - Full SSN, or ID document scan for non-US persons or if SSN can’t be verified
  - Title
  - Phone number                                   |
| Companies (LLCs, corporations, non-profits, partnerships, and so on) | Entity details:
  - Business names (customer facing and legal)
  - Legal entity type
  - Business address
  - Business phone number
  - Product or service description
  - Industry or Merchant category code
  - Tax ID Number (EIN)
  - Financial Accounts for platforms TOS acceptance
  - Stripe TOS acceptance
Owner/representative details:
  - Legal name
  - Date of birth
  - Email address
  - Residential address
  - Phone number
  - Title
  - Percent ownership of company
  - Full SSN, or ID document scan for non-US persons or if SSN can’t be verified |

### Completion

The connected account onboarding process is complete when you receive an `account.updated` [webhook](https://docs.stripe.com/webhooks.md) confirming the following fields on your connected account:

```
{
  "object": {
    "object": "account",
    "id": "acct_1234",
    "capabilities": {
      "treasury": "active",
      "card_issuing": "active", // Only appears if requesting the `card_issuing` capability.
      "us_bank_account_ach_payments": "active", // Only appears if requesting the `us_bank_account_ach_payments` capability.
    },
    ...
  }
}
```

Account onboarding latency when your platform’s bank partner is Evolve Bank & Trust is less than 5 minutes.

### Updates to requirements

To adapt to changes in financial regulations, Stripe must occasionally update information collection requirements for Financial Accounts for platforms. The `requirements.eventually_due` array on the `Account` object captures the updated information required by these regulation changes. Learn more about the [requirements](https://docs.stripe.com/api/accounts/object.md#account_object-requirements) hash.
