# Required verification information

View requirements you must collect from connected accounts based on your setup.

#### Processing live charges and receiving payouts

In the UAE, company documents such as the [Trade License](https://docs.stripe.com/api/accounts/create.md#create_account-documents-company_license) and [Proof of Bank Account](https://docs.stripe.com/api/accounts/create.md#create_account-documents-bank_account_ownership_verification) as well as relevant identity documents must be verified before a connected account can start processing live charges and receiving *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). For all businesses except sole establishments and free zone establishments, the [Memorandum of Association](https://docs.stripe.com/api/accounts/create.md#create_account-documents-company_memorandum_of_association) must be verified as well.

#### Uploading identity documents

For the company representative, beneficial owners and executives, we require the following identity documents for verification:

- Passport: all individuals
- Emirates ID: UAE nationals & UAE residents
- Residence visa: foreign nationals who are resident in the UAE

The Emirates ID can be provided in the parameter called [verification.document](https://docs.stripe.com/api/persons/create.md#create_person-verification-document). Passports and residence visas should be provided under a separate parameter called [documents](https://docs.stripe.com/api/persons/create.md#create_person-documents).

#### Keeping up to date with expired verification documents

In the UAE, Stripe is required to keep up to date with a company’s [Trade License](https://docs.stripe.com/api/accounts/update.md#update_account-documents-company_license) in addition to the primary identity document of the company’s representative, beneficial owners and executives. The primary identity document is either the Emirates ID for UAE nationals and residents, otherwise it is an individual’s [Passport](https://docs.stripe.com/api/persons/update.md#update_person-documents-passport). Companies will have up to 28 days after the expiry date of these documents to provide an updated version. Expired documents will appear under [company requirements](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-currently_due) or [individual requirements](https://docs.stripe.com/api/persons/object.md#person_object-requirements-currently_due) and marked as currently due for two weeks before capabilities become disabled.

#### Ultimate Beneficial Owners

Stripe is required to verify all the [beneficial owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) of a business. These are the individuals who own 25% or more of the primary business. If a [holding company](https://support.stripe.com/questions/beneficial-ownership-by-a-trust-holding-company-or-other-legal-entity) has 25% or more ownership of the business, then the Memorandum of Association of this holding company as well that of the primary business must be uploaded. These documents must show the persons where [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) is set to true.

#### Additional information on the company representative

This connected account needs to be activated by a person, known as the company representative, with significant responsibility to control, manage, or direct the organization and is authorized by the organization to agree to Stripe’s terms. The representative must either be [an owner or an executive](https://support.stripe.com/questions/beneficial-owner-and-director-definitions), which you specify by setting [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) to `true` or [relationship.executive](https://docs.stripe.com/api/persons/object.md#person_object-relationship-executive) to `true`. For a sole establishment or free zone establishment, the account must be activated by the owner of the business.

#### VAT Information

Stripe doesn’t charge UAE VAT on Stripe fees to customers located in the UAE, where a valid UAE VAT ID has been provided. Local UAE VAT self-assessment obligations may be triggered upon receipt of a monthly *invoice* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice) from Stripe. Stripe does charge UAE VAT at 5% on Stripe fees to customers located in the UAE, where a valid UAE VAT ID hasn’t been provided.

#### Power of Attorney

If the company representative doesn’t appear on the company’s Trade License or the Memorandum of Association, then you must upload a [Power of Attorney](https://docs.stripe.com/api/persons/update.md#update_person-documents-company_authorization) that shows that the company representative has the authority to act on behalf of the company or a notarized letter of authorization.

#### Supported business structures 

In the UAE, the only possible business type is `company` and the following [business structures](https://docs.stripe.com/connect/identity-verification.md#business-structure) are accepted:

- `sole_establishment`
- `free_zone_establishment`
- `llc`
- `free_zone_llc`

#### Additional information on the representative 

If Stripe is unable to verify the representative, you need to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). This can be collected with the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

#### Additional information on the owner

If Stripe is unable to verify the owner, you need to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). This can be collected with the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

#### Additional information on the individual 

If Stripe is unable to verify the individual, you need to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). This can be collected with the [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back) arguments.

#### Additional information on bank accounts

We’ll verify that the legal owner of each payout bank account [matches that of the Stripe account](https://support.stripe.com/questions/bank-account-ownership-verification).

If Stripe can’t verify the owner of the bank account, we’ll transition the status of the ExternalAccount to `verification_failed`. You’ll need to collect a scan of a cancelled check or bank statement to prove the legal owner of the bank account. Collect this information with the [documents.bank_account_ownership_verification.files](https://docs.stripe.com/api/accounts/update.md#update_account-external_account-documents-bank_account_ownership_verification) argument.

#### Provide ID document for the representative

You must provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) for the representative. To collect this scan, use the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

Identity verification documents must be issued in Japan and show the representative’s residency status.

#### Provide ID document for the individual

You must collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) for an individual. To collect this scan, use the [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back) arguments.

Identity verification documents must be issued in Japan and show the individual’s residency status.

#### Special considerations

Collecting information for Japanese accounts is unique in that both *kana* and *kanji* language variations are required for a number of parameters:

- `first_name_kana`
- `first_name_kanji`
- `last_name_kana`
- `last_name_kanji`
- `name_kana`
- `name_kanji`
- `address_kana`
- `address_kanji`

You need to submit information for these parameters instead of their counterparts (that is, instead of `first_name`, `last_name`, and so forth). It might seem counterintuitive to provide two arguments that represent the same onboarding requirement, but Stripe can’t verify a Japanese account until we’ve received information for both language variations. These variations may be composed of full- or half-width hiragana, katakana, or Latin characters, with kanji-specific API parameters also allowing for kanji characters.

#### Japanese addresses

Both kana and kanji language variations apply to Japanese address requirements as well.

`postal_code` is always required when providing a Japanese address of either language variation. Stripe validates submitted addresses, and for a valid `postal_code`, we attempt to automatically fill attributes for matching `state`, `city`, and `town` for *both* `address_kana` and `address_kanji`.

Requests with address details that are incompatible with the provided `postal_code` fail.

`line2` should contain the building name in addition to the room number if applicable. This attribute can be omitted when the address doesn’t contain building details.

Here’s an example representation of a Japanese address, with explanations for how each part maps to its corresponding Stripe API attribute:

```json
// 〒150-0001 東京都渋谷区神宮前1-5-8 神宮前タワービルディング22F
{
  "country": "JP",
  "legal_entity": {
    "address_kana": {
      "country": "JP", // 2-letter country code
      "postal_code": "1500001", // Zip/Postal Code
      "state": "ﾄｳｷﾖｳﾄ", // Prefecture
      "city": "ｼﾌﾞﾔ", // City/Ward
      "town": "ｼﾞﾝｸﾞｳﾏｴ 1-", // Town/cho-me
      "line1": "5-8", // Block/Building number
      "line2": "ｼﾞﾝｸﾞｳﾏｴﾀﾜｰﾋﾞﾙﾃﾞｨﾝｸﾞ22F", // Building details (optional)
    },
    "address_kanji": {
      "country": "JP", // 2-letter country code
      "postal_code": "１５００００１", // Zip/Postal Code
      "state": "東京都", // Prefecture
      "city": "渋谷区", // City/Ward
      "town": "神宮前　１丁目", // Town/cho-me (no kanji numerals)
      "line1": "５－８", // Block/Building number
      "line2": "神宮前タワービルディング22F", // Building details (optional)
    }
  }
}
```

#### Statement descriptors

Statement descriptors explain charges or payments and include information that banks and card networks require to help customers understand their statements.

We recommend setting the [static](https://docs.stripe.com/get-started/account/statement-descriptors.md#static) components of statement descriptors in all three supported scripts (kanji, kana, and Latin characters) for Japanese connected accounts.

|                                     | PARAMETER                                                |
| ----------------------------------- | -------------------------------------------------------- |
| Statement descriptor                | settings.payments.statement_descriptor                   |
| Statement descriptor (kanji)        | settings.payments.statement_descriptor_kanji             |
| Statement descriptor (kana)         | settings.payments.statement_descriptor_kana              |
| Statement descriptor prefix         | settings.card_payments.statement_descriptor_prefix       |
| Statement descriptor prefix (kanji) | settings.card_payments.statement_descriptor_prefix_kanji |
| Statement descriptor prefix (kana)  | settings.card_payments.statement_descriptor_prefix_kana  |

You can set these fields with [API](https://docs.stripe.com/api/accounts/create.md#create_account-settings-payments-statement_descriptor).

```curl
curl https://api.stripe.com/v1/accounts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d type=custom \
  -d country=JP \
  -d business_type=company \
  -d "capabilities[card_payments][requested]"=true \
  -d "capabilities[transfers][requested]"=true \
  -d "settings[payments][statement_descriptor]"="example descriptor" \
  -d "settings[payments][statement_descriptor_kanji]"="漢字明細" \
  -d "settings[payments][statement_descriptor_kana]"="カナメイサイ"
```

```cli
stripe accounts create  \
  --type=custom \
  --country=JP \
  --business-type=company \
  -d "capabilities[card_payments][requested]"=true \
  -d "capabilities[transfers][requested]"=true \
  -d "settings[payments][statement_descriptor]"="example descriptor" \
  -d "settings[payments][statement_descriptor_kanji]"="漢字明細" \
  -d "settings[payments][statement_descriptor_kana]"="カナメイサイ"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.create({
  type: 'custom',
  country: 'JP',
  business_type: 'company',
  capabilities: {
    card_payments: {requested: true},
    transfers: {requested: true},
  },
  settings: {
    payments: {
      statement_descriptor: 'example descriptor',
      statement_descriptor_kanji: '漢字明細',
      statement_descriptor_kana: 'カナメイサイ',
    },
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.create({
  "type": "custom",
  "country": "JP",
  "business_type": "company",
  "capabilities": {
    "card_payments": {"requested": True},
    "transfers": {"requested": True},
  },
  "settings": {
    "payments": {
      "statement_descriptor": "example descriptor",
      "statement_descriptor_kanji": "漢字明細",
      "statement_descriptor_kana": "カナメイサイ",
    },
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->create([
  'type' => 'custom',
  'country' => 'JP',
  'business_type' => 'company',
  'capabilities' => [
    'card_payments' => ['requested' => true],
    'transfers' => ['requested' => true],
  ],
  'settings' => [
    'payments' => [
      'statement_descriptor' => 'example descriptor',
      'statement_descriptor_kanji' => '漢字明細',
      'statement_descriptor_kana' => 'カナメイサイ',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountCreateParams params =
  AccountCreateParams.builder()
    .setType(AccountCreateParams.Type.CUSTOM)
    .setCountry("JP")
    .setBusinessType(AccountCreateParams.BusinessType.COMPANY)
    .setCapabilities(
      AccountCreateParams.Capabilities.builder()
        .setCardPayments(
          AccountCreateParams.Capabilities.CardPayments.builder()
            .setRequested(true)
            .build()
        )
        .setTransfers(
          AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build()
        )
        .build()
    )
    .setSettings(
      AccountCreateParams.Settings.builder()
        .setPayments(
          AccountCreateParams.Settings.Payments.builder()
            .setStatementDescriptor("example descriptor")
            .setStatementDescriptorKanji("漢字明細")
            .setStatementDescriptorKana("カナメイサイ")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.create({
  type: 'custom',
  country: 'JP',
  business_type: 'company',
  capabilities: {
    card_payments: {
      requested: true,
    },
    transfers: {
      requested: true,
    },
  },
  settings: {
    payments: {
      statement_descriptor: 'example descriptor',
      statement_descriptor_kanji: '漢字明細',
      statement_descriptor_kana: 'カナメイサイ',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountCreateParams{
  Type: stripe.String(stripe.AccountTypeCustom),
  Country: stripe.String("JP"),
  BusinessType: stripe.String(stripe.AccountBusinessTypeCompany),
  Capabilities: &stripe.AccountCreateCapabilitiesParams{
    CardPayments: &stripe.AccountCreateCapabilitiesCardPaymentsParams{
      Requested: stripe.Bool(true),
    },
    Transfers: &stripe.AccountCreateCapabilitiesTransfersParams{
      Requested: stripe.Bool(true),
    },
  },
  Settings: &stripe.AccountCreateSettingsParams{
    Payments: &stripe.AccountCreateSettingsPaymentsParams{
      StatementDescriptor: stripe.String("example descriptor"),
      StatementDescriptorKanji: stripe.String("漢字明細"),
      StatementDescriptorKana: stripe.String("カナメイサイ"),
    },
  },
}
result, err := sc.V1Accounts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountCreateOptions
{
    Type = "custom",
    Country = "JP",
    BusinessType = "company",
    Capabilities = new AccountCapabilitiesOptions
    {
        CardPayments = new AccountCapabilitiesCardPaymentsOptions { Requested = true },
        Transfers = new AccountCapabilitiesTransfersOptions { Requested = true },
    },
    Settings = new AccountSettingsOptions
    {
        Payments = new AccountSettingsPaymentsOptions
        {
            StatementDescriptor = "example descriptor",
            StatementDescriptorKanji = "漢字明細",
            StatementDescriptorKana = "カナメイサイ",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Create(options);
```

See [Japanese statement descriptors](https://docs.stripe.com/get-started/account/statement-descriptors.md#set-japanese-statement-descriptors) for more details.

#### Additional information on the account 

If Stripe is unable to verify the business entity, the entity doesn’t have a `company.tax_id`, or there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you need to collect a proof of entity document to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using the [company.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-front) and [company.verification.document.back](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-back) arguments.

#### Companies with the card_payments capability

`company` refers to these types of entities:

- Sociedad Anónima (S.A.)
- Sociedad de Responsabilidad Limitada (S. de R.L.)
- Sociedad Anónima Promotora de Inversión (S.A.P.I.)
- Sociedad por Acciones Simplificada (S.A.S.)

#### Additional information on the individual 

If the individual fails verification, doesn’t have an `individual.id_number`, or there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), then an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) scan is required to enable payouts. You can collect that information using the [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back) arguments.

#### Additional information on the representative 

A person known as a representative must activate this connected account. The representative must be an authorized signatory with legal powers to represent the company as set forth under the relevant corporate documents, and must be authorized to agree to Stripe’s terms.

If Stripe is unable to verify the representative, the representative doesn’t have a [representative.id_number](https://docs.stripe.com/api/persons/update.md#update_person-id_number), or there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

#### Additional information on owners 

You must collect information on all [owners](https://support.stripe.com/questions/beneficial-owner-and-director-definition) with more than 25% ownership of the company. When you have finished collecting the required owner information, you must inform Stripe by setting [company.owners_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-owners_provided) to `true`.

If Stripe is unable to verify an owner, an owner doesn’t have an [owners.id_number](https://docs.stripe.com/api/persons/update.md#update_person-id_number), or there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

Optionally, you can collect ownership information on each person using the [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership) arguments.

#### Additional information on the account 

If Stripe is unable to verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using the [company.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-front) and [company.verification.document.back](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-back) arguments.

#### Additional information on the individual 

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents), an address document, or both to enable payouts. That can happen if Stripe is unable to verify the individual or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using the [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back) arguments.

#### Additional information on the representative 

A person known as a representative must activate this connected account. The representative must be a [beneficial owner](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who is authorized to sign for the company. Indicate that relationship to Stripe by setting [relationship.executive](https://docs.stripe.com/api/persons/object.md#person_object-relationship-executive) to `true`, or, if the representative owns 25% or more of the company, by setting [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) to `true`.

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe is unable to verify the representative or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

Optionally, you can collect the representative’s ownership information using the [relationship.representative](https://docs.stripe.com/api/persons/object.md#person_object-relationship-representative) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership) arguments.

#### Additional information on directors 

For companies (excluding partnerships), you must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Directors are members of the governing board of the company. When you have finished collecting the required information from all directors, or if your company doesn’t have any directors, you must notify Stripe by setting [company.directors_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-directors_provided) to `true`.

If there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

#### Additional information on beneficial owners (for both executives and owners) 

You must collect information on all [beneficial owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Beneficial owners are persons who exercise significant management control over the company (executives) or who own 25% or more of the company (owners). When you have finished collecting the required information from all beneficial owners, you must notify Stripe by setting both [company.owners_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-owners_provided) and [company.executives_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-executives_provided) to `true`.

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe is unable to verify a beneficial owner or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) arguments.

Optionally, you can collect ownership information on each person who owns 25% or more of the company using the [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership) arguments.

#### Additional information on the account 

If Stripe can’t verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using:

- [identity.business_details.documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-response-identity-business_details-documents-primary_verification-front_back-front)
- [identity.business_details.documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-response-identity-business_details-documents-primary_verification-front_back-back)

#### Additional information on the individual 

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the individual or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns).

Collect ID information using:

- [identity.individual.documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification-front_back-front)
- [identity.individual.documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification-front_back-back)

#### Additional information on the representative 

A person known as a *representative* must activate this connected account. This person must be a [beneficial owner](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who is authorized to sign for the company. Indicate this relationship to Stripe by setting [relationship.executive](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-executive) to true, or, if the representative owns 25% or more of the company, by setting [relationship.owner](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner) to true.

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the representative or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using:

- [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-documents-primary_verification-front_back-front)
- [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-documents-primary_verification-front_back-back)

Optionally, you can collect the representative’s ownership information using [relationship.representative](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-representative) and [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-percent_ownership).

#### Additional information on directors 

For companies (excluding partnerships), you must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Directors are members of the governing board of the company. When you have finished collecting the required information from all directors, or if your company doesn’t have any directors, you must inform Stripe by setting [identity.attestations.persons_provided.directors](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-attestations-persons_provided-directors) to true.

If there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. Collect ID information using the [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-front) and [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-back) parameters.

#### Additional information on beneficial owners (for both executives and owners) 

You must collect information on all [beneficial owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Beneficial owners are persons who exercise significant management control over the company (executives) or who own 25% or more of the company (owners). When you have finished collecting the required information from all beneficial owners, you must inform Stripe by setting both of the following to true:

- [identity.attestations.persons_provided.owners](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-attestations-persons_provided-owners)
- [identity.attestations.persons_provided.executives](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-attestations-persons_provided-executives)

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify a beneficial owner or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using the [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-front) and [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-back) parameters.

Optionally, you can collect ownership information on each person who owns 25% or more of the company using [relationship.owner](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-percent_ownership).

#### Additional information on the account 

If Stripe can’t verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=entity#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using the [identity.business_details.documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-primary_verification-front_back-front) and [identity.business_details.documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-primary_verification-front_back-back) parameters.

#### Additional information on the representative 

If Stripe can’t verify the representative, they need to provide proof of liveness, which entails taking a selfie and uploading a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=identity#acceptable-verification-documents) using [Stripe Identity](https://docs.stripe.com/identity.md). Your platform needs to integrate with [Connect Onboarding](https://stripe.com/connect/onboarding) to satisfy this requirement.

Alternatively, you can provide a scan of a government-issued ID document and a scan of an address document. To collect a government-issued ID document, use the [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-front) and [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-back) parameters. To collect an address document, use the [documents.secondary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-secondary_verification-front_back-front) and [documents.secondary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-secondary_verification-front_back-back) parameters.

#### Additional information on the individual 

Individuals that Stripe can’t verify must provide proof of liveness, which entails taking a selfie and uploading a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=identity#acceptable-verification-documents) using [Stripe Identity](https://docs.stripe.com/identity.md). Your platform needs to integrate with [Connect Onboarding](https://stripe.com/connect/onboarding) to allow such individuals to complete this requirement.

Alternatively, your platform can collect scans of an individual’s ID and address documents and [upload them](https://docs.stripe.com/api/files/create.md) to Stripe. After uploading, submit the individual’s ID document with the [identity.individual.documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification-front_back-front) and [identity.individual.documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification-front_back-back) parameters. To collect an address document, use the [identity.individual.documents.secondary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-secondary_verification-front_back-front) and [identity.individual.documents.secondary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-secondary_verification-front_back-back) parameters.

#### Additional information on owners 

You must collect information on all [owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Owners are any individual who owns 25% or more of the company. When you finish collecting the required information from all owners, set [identity.attestations.persons_provided.owners](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-attestations-persons_provided-owners) to true. This lets Stripe know that you have completed this requirement.

Optionally, you can collect ownership information on each person who owns 25% or more of the company with [relationship.owner](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-percent_ownership).

#### Additional information on directors 

You must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). We check the [director](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) information you supply against the registry and results in one of these outcomes:

- The business is found in the registry, and the information matches. The account fully onboards, and requires no additional action.
- The business is found in the registry, but the director information doesn’t match. You must upload a [proof of registration document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=relationship#acceptable-verification-documents) using the [identity.business_details.documents.proof_of_registration.files](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-proof_of_registration-files) parameter. Set the `File`’s `purpose` parameter to `account_requirement`.

#### curl

```bash
curl https://files.stripe.com/v1/files \
  -u <<YOUR_SECRET_KEY>>: \
  -H "Stripe-Account: {{CONNECTED_STRIPE_ACCOUNT_ID}}" \
  -F "purpose"="account_requirement" \
  -F "file"="@/path/to/a/file"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

Stripe::File.create({
  purpose: 'account_requirement',
  file: File.new('/path/to/a/file.jpg'),
}, {
  stripe_account: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
})
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

with open("/path/to/a/file.jpg", "rb") as fp:
  stripe.File.create(
    purpose='account_requirement',
    file=fp,
    stripe_account='{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  )
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

\Stripe\File::create([
  'purpose' => 'account_requirement',
  'file' => fopen('/path/to/a/file.jpg', 'r'),
], [
  'stripe_account' => '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
]);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

FileCreateParams params =
  FileCreateParams.builder()
    .setPurpose(FileCreateParams.Purpose.account_requirement)
    .setFile(new java.io.File("/path/to/a/file.jpg"))
    .build();

RequestOptions requestOptions =
  RequestOptions.builder()
    .setStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
    .build();

File file = File.create(params);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const fs = require('fs');
const file = await stripe.files.create({
  purpose: 'account_requirement',
  file: {
    data: fs.readFileSync('/path/to/a/file.jpg'),
    name: 'file_name.jpg',
    type: 'application/octet-stream',
  },
}, {
  stripeAccount: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
});
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

fp, _ := os.Open("/path/to/a/success.png")
params := &stripe.FileParams{
  FileReader: fp,
  Filename: stripe.String("success.png"),
  Purpose: stripe.String(string(stripe.FileUploadPurposeAccountRequirement)),
}
params.SetStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
f, _ := file.New(params)
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var filename = "/path/to/a/success.png";
using (FileStream stream = System.IO.File.Open(filename, FileMode.Open))
{
  var options = new FileCreateOptions
  {
    File = stream,
    Purpose = FilePurpose.AccountRequirement,
  };
  var service = new FileService();
  var upload = service.Create(options);
}
```

This request uploads the file and returns a token:

```json
{
  "id": "file_5dtoJkOhAxrMWb",
  "created": 1403047735,
  "size": 4908
}
```

You can then use the token’s `id` value to attach the file to a connected account for identity verification.

```curl
curl -X POST https://api.stripe.com/v2/core/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -H "Authorization: Bearer <<YOUR_SECRET_KEY>>" \
  -H "Stripe-Version: preview" \
  --json '{
    "include": [
        "identity"
    ],
    "identity": {
        "business_details": {
            "documents": {
                "proof_of_registration": {
                    "files": [
                        "file_5dtoJkOhAxrMWb"
                    ]
                }
            }
        }
    }
  }'
```

```cli
stripe v2 core accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  --include=identity \
  --identity.business-details.documents.proof-of-registration.files=file_5dtoJkOhAxrMWb
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      business_details: {
        documents: {proof_of_registration: {files: ['file_5dtoJkOhAxrMWb']}},
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "include": ["identity"],
    "identity": {
      "business_details": {
        "documents": {"proof_of_registration": {"files": ["file_5dtoJkOhAxrMWb"]}},
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->v2->core->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'include' => ['identity'],
    'identity' => [
      'business_details' => [
        'documents' => ['proof_of_registration' => ['files' => ['file_5dtoJkOhAxrMWb']]],
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
    .addInclude(AccountUpdateParams.Include.IDENTITY)
    .setIdentity(
      AccountUpdateParams.Identity.builder()
        .setBusinessDetails(
          AccountUpdateParams.Identity.BusinessDetails.builder()
            .setDocuments(
              AccountUpdateParams.Identity.BusinessDetails.Documents.builder()
                .setProofOfRegistration(
                  AccountUpdateParams.Identity.BusinessDetails.Documents.ProofOfRegistration.builder()
                    .addFile("file_5dtoJkOhAxrMWb")
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

Account account =
  client.v2().core().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      business_details: {
        documents: {
          proof_of_registration: {
            files: ['file_5dtoJkOhAxrMWb'],
          },
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.V2CoreAccountUpdateParams{
  Include: []*string{stripe.String("identity")},
  Identity: &stripe.V2CoreAccountUpdateIdentityParams{
    BusinessDetails: &stripe.V2CoreAccountUpdateIdentityBusinessDetailsParams{
      Documents: &stripe.V2CoreAccountUpdateIdentityBusinessDetailsDocumentsParams{
        ProofOfRegistration: &stripe.V2CoreAccountUpdateIdentityBusinessDetailsDocumentsProofOfRegistrationParams{
          Files: []*string{stripe.String("file_5dtoJkOhAxrMWb")},
        },
      },
    },
  },
}
result, err := sc.V2CoreAccounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.V2.Core.AccountUpdateOptions
{
    Include = new List<string> { "identity" },
    Identity = new Stripe.V2.Core.AccountUpdateIdentityOptions
    {
        BusinessDetails = new Stripe.V2.Core.AccountUpdateIdentityBusinessDetailsOptions
        {
            Documents = new Stripe.V2.Core.AccountUpdateIdentityBusinessDetailsDocumentsOptions
            {
                ProofOfRegistration = new Stripe.V2.Core.AccountUpdateIdentityBusinessDetailsDocumentsProofOfRegistrationOptions
                {
                    Files = new List<string> { "file_5dtoJkOhAxrMWb" },
                },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V2.Core.Accounts;
Stripe.V2.Core.Account account = service.Update(
    "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
    options);
```

#### Additional information on registration statuses

If Stripe can’t verify the registration status of the charity, you need to collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=entity#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Upload it using the [identity.business_details.documents.company_registration_verification.files](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-company_registration_verification-files) parameter.

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [identity.attestations.ownership_declaration](https://docs.stripe.com/api/v2/core/accounts/update.md?lang=curl&api-version=preview#v2_update_accounts-identity-attestations-ownership_declaration) hash.

```curl
curl -X POST https://api.stripe.com/v2/core/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -H "Authorization: Bearer <<YOUR_SECRET_KEY>>" \
  -H "Stripe-Version: preview" \
  --json '{
    "include": [
        "identity"
    ],
    "identity": {
        "attestations": {
            "ownership_declaration": {
                "date": "2022-09-18T13:22:18.123Z",
                "ip": "8.8.8.8",
                "user_agent": "Mozilla/5.0"
            }
        }
    }
  }'
```

```cli
stripe v2 core accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  --include=identity \
  --identity.attestations.ownership-declaration.date="2022-09-18T13:22:18.123Z" \
  --identity.attestations.ownership-declaration.ip="8.8.8.8" \
  --identity.attestations.ownership-declaration.user-agent="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "include": ["identity"],
    "identity": {
      "attestations": {
        "ownership_declaration": {
          "date": "2022-09-18T13:22:18.123Z",
          "ip": "8.8.8.8",
          "user_agent": "Mozilla/5.0",
        },
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->v2->core->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'include' => ['identity'],
    'identity' => [
      'attestations' => [
        'ownership_declaration' => [
          'date' => '2022-09-18T13:22:18.123Z',
          'ip' => '8.8.8.8',
          'user_agent' => 'Mozilla/5.0',
        ],
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
    .addInclude(AccountUpdateParams.Include.IDENTITY)
    .setIdentity(
      AccountUpdateParams.Identity.builder()
        .setAttestations(
          AccountUpdateParams.Identity.Attestations.builder()
            .setOwnershipDeclaration(
              AccountUpdateParams.Identity.Attestations.OwnershipDeclaration.builder()
                .setDate(Instant.parse("2022-09-18T13:22:18.123Z"))
                .setIp("8.8.8.8")
                .setUserAgent("Mozilla/5.0")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

Account account =
  client.v2().core().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.V2CoreAccountUpdateParams{
  Include: []*string{stripe.String("identity")},
  Identity: &stripe.V2CoreAccountUpdateIdentityParams{
    Attestations: &stripe.V2CoreAccountUpdateIdentityAttestationsParams{
      OwnershipDeclaration: &stripe.V2CoreAccountUpdateIdentityAttestationsOwnershipDeclarationParams{
        Date: stripe.Time(time.Now()),
        IP: stripe.String("8.8.8.8"),
        UserAgent: stripe.String("Mozilla/5.0"),
      },
    },
  },
}
result, err := sc.V2CoreAccounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.V2.Core.AccountUpdateOptions
{
    Include = new List<string> { "identity" },
    Identity = new Stripe.V2.Core.AccountUpdateIdentityOptions
    {
        Attestations = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOptions
        {
            OwnershipDeclaration = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOwnershipDeclarationOptions
            {
                Date = DateTimeOffset.Parse("2022-09-18T13:22:18.123Z").UtcDateTime,
                Ip = "8.8.8.8",
                UserAgent = "Mozilla/5.0",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V2.Core.Accounts;
Stripe.V2.Core.Account account = service.Update(
    "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
    options);
```

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [identity.attestations.ownership_declaration](https://docs.stripe.com/api/v2/core/accounts/update.md?lang=curl&api-version=preview#v2_update_accounts-identity-attestations-ownership_declaration) hash.

```curl
curl -X POST https://api.stripe.com/v2/core/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -H "Authorization: Bearer <<YOUR_SECRET_KEY>>" \
  -H "Stripe-Version: preview" \
  --json '{
    "include": [
        "identity"
    ],
    "identity": {
        "attestations": {
            "ownership_declaration": {
                "date": "2022-09-18T13:22:18.123Z",
                "ip": "8.8.8.8",
                "user_agent": "Mozilla/5.0"
            }
        }
    }
  }'
```

```cli
stripe v2 core accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  --include=identity \
  --identity.attestations.ownership-declaration.date="2022-09-18T13:22:18.123Z" \
  --identity.attestations.ownership-declaration.ip="8.8.8.8" \
  --identity.attestations.ownership-declaration.user-agent="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "include": ["identity"],
    "identity": {
      "attestations": {
        "ownership_declaration": {
          "date": "2022-09-18T13:22:18.123Z",
          "ip": "8.8.8.8",
          "user_agent": "Mozilla/5.0",
        },
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->v2->core->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'include' => ['identity'],
    'identity' => [
      'attestations' => [
        'ownership_declaration' => [
          'date' => '2022-09-18T13:22:18.123Z',
          'ip' => '8.8.8.8',
          'user_agent' => 'Mozilla/5.0',
        ],
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
    .addInclude(AccountUpdateParams.Include.IDENTITY)
    .setIdentity(
      AccountUpdateParams.Identity.builder()
        .setAttestations(
          AccountUpdateParams.Identity.Attestations.builder()
            .setOwnershipDeclaration(
              AccountUpdateParams.Identity.Attestations.OwnershipDeclaration.builder()
                .setDate(Instant.parse("2022-09-18T13:22:18.123Z"))
                .setIp("8.8.8.8")
                .setUserAgent("Mozilla/5.0")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

Account account =
  client.v2().core().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.V2CoreAccountUpdateParams{
  Include: []*string{stripe.String("identity")},
  Identity: &stripe.V2CoreAccountUpdateIdentityParams{
    Attestations: &stripe.V2CoreAccountUpdateIdentityAttestationsParams{
      OwnershipDeclaration: &stripe.V2CoreAccountUpdateIdentityAttestationsOwnershipDeclarationParams{
        Date: stripe.Time(time.Now()),
        IP: stripe.String("8.8.8.8"),
        UserAgent: stripe.String("Mozilla/5.0"),
      },
    },
  },
}
result, err := sc.V2CoreAccounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.V2.Core.AccountUpdateOptions
{
    Include = new List<string> { "identity" },
    Identity = new Stripe.V2.Core.AccountUpdateIdentityOptions
    {
        Attestations = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOptions
        {
            OwnershipDeclaration = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOwnershipDeclarationOptions
            {
                Date = DateTimeOffset.Parse("2022-09-18T13:22:18.123Z").UtcDateTime,
                Ip = "8.8.8.8",
                UserAgent = "Mozilla/5.0",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V2.Core.Accounts;
Stripe.V2.Core.Account account = service.Update(
    "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
    options);
```

#### Additional information on the account 

If Stripe can’t verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using the [identity.business_details.documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-primary_verification-front_back-front) and [identity.business_details.documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-primary_verification-front_back-back) parameters.

#### Additional information on the individual 

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents), an address document, or both to enable payouts. That can happen if Stripe can’t verify the individual or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

Collect ID information using the [identity.individual.documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification-front_back-front) and [identity.individual.documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification-front_back-back) parameters, and address information using the [identity.individual.documents.secondary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-secondary_verification-front_back-front) and [identity.individual.documents.secondary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-secondary_verification-front_back-back) parameters.

#### Additional information on the representative 

A person known as a representative must activate this connected account. The representative must be a [beneficial owner](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who is authorized to sign for the company. Indicate this relationship to Stripe by setting [relationship.executive](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-executive) to true, or, if the representative owns 25% or more of the company, by setting [relationship.owner](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner) to true.

You might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) and an [address document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the representative’s provided information or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

Additionally, for partnerships you must provide a [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-percent_ownership) value.

You can collect ID information with the [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-documents-primary_verification-front_back-front) and [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-documents-primary_verification-front_back-back) parameters.

#### Additional information on directors 

For companies, excluding partnerships, you must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Directors are members of the governing board of the company. When you have finished collecting the required information from all directors, or if your company doesn’t have any directors, you must inform Stripe by setting [identity.attestations.persons_provided.directors](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-attestations-persons_provided-directors) to true.

You might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) and an [address document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the director’s provided information or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

Collect ID information using the [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-front) and [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-back) parameters, and address information using the [documents.secondary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-secondary_verification-front_back-front) and [documents.secondary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-secondary_verification-front_back-back) parameters.

#### Additional information on beneficial owners (for both executives and owners) 

You must collect information on all [beneficial owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Beneficial owners are persons who exercise significant management control over the company (executives) or who own 25% or more of the company (owners). When you have finished collecting the required information from all beneficial owners, you must inform Stripe by setting both [identity.attestations.persons_provided.owners](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-attestations-persons_provided-owners) and [identity.attestations.persons_provided.executives](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-attestations-persons_provided-executives) to true.

You might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) and an [address document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the beneficial owner’s provided information or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

Collect ID information using the [documents.primary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-front) and [documents.primary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification-front_back-back) parameters, and address information using the [documents.secondary_verification.front_back.front](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-secondary_verification-front_back-front) and [documents.secondary_verification.front_back.back](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-secondary_verification-front_back-back) parameters.

Optionally, you can collect ownership information on each person who owns 25% or more of the company with [relationship.owner](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-percent_ownership).

Additionally, for partnerships you need to provide a [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-percent_ownership) value for any owners added to the account.

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [identity.attestations.ownership_declaration](https://docs.stripe.com/api/v2/core/accounts/update.md?lang=curl&api-version=preview#v2_update_accounts-identity-attestations-ownership_declaration) hash.

```curl
curl -X POST https://api.stripe.com/v2/core/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -H "Authorization: Bearer <<YOUR_SECRET_KEY>>" \
  -H "Stripe-Version: preview" \
  --json '{
    "include": [
        "identity"
    ],
    "identity": {
        "attestations": {
            "ownership_declaration": {
                "date": "2022-09-18T13:22:18.123Z",
                "ip": "8.8.8.8",
                "user_agent": "Mozilla/5.0"
            }
        }
    }
  }'
```

```cli
stripe v2 core accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  --include=identity \
  --identity.attestations.ownership-declaration.date="2022-09-18T13:22:18.123Z" \
  --identity.attestations.ownership-declaration.ip="8.8.8.8" \
  --identity.attestations.ownership-declaration.user-agent="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "include": ["identity"],
    "identity": {
      "attestations": {
        "ownership_declaration": {
          "date": "2022-09-18T13:22:18.123Z",
          "ip": "8.8.8.8",
          "user_agent": "Mozilla/5.0",
        },
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->v2->core->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'include' => ['identity'],
    'identity' => [
      'attestations' => [
        'ownership_declaration' => [
          'date' => '2022-09-18T13:22:18.123Z',
          'ip' => '8.8.8.8',
          'user_agent' => 'Mozilla/5.0',
        ],
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
    .addInclude(AccountUpdateParams.Include.IDENTITY)
    .setIdentity(
      AccountUpdateParams.Identity.builder()
        .setAttestations(
          AccountUpdateParams.Identity.Attestations.builder()
            .setOwnershipDeclaration(
              AccountUpdateParams.Identity.Attestations.OwnershipDeclaration.builder()
                .setDate(Instant.parse("2022-09-18T13:22:18.123Z"))
                .setIp("8.8.8.8")
                .setUserAgent("Mozilla/5.0")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

Account account =
  client.v2().core().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.V2CoreAccountUpdateParams{
  Include: []*string{stripe.String("identity")},
  Identity: &stripe.V2CoreAccountUpdateIdentityParams{
    Attestations: &stripe.V2CoreAccountUpdateIdentityAttestationsParams{
      OwnershipDeclaration: &stripe.V2CoreAccountUpdateIdentityAttestationsOwnershipDeclarationParams{
        Date: stripe.Time(time.Now()),
        IP: stripe.String("8.8.8.8"),
        UserAgent: stripe.String("Mozilla/5.0"),
      },
    },
  },
}
result, err := sc.V2CoreAccounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.V2.Core.AccountUpdateOptions
{
    Include = new List<string> { "identity" },
    Identity = new Stripe.V2.Core.AccountUpdateIdentityOptions
    {
        Attestations = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOptions
        {
            OwnershipDeclaration = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOwnershipDeclarationOptions
            {
                Date = DateTimeOffset.Parse("2022-09-18T13:22:18.123Z").UtcDateTime,
                Ip = "8.8.8.8",
                UserAgent = "Mozilla/5.0",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V2.Core.Accounts;
Stripe.V2.Core.Account account = service.Update(
    "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
    options);
```

### Universal Beneficial Ownership Verification 

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, no additional action is required. A discrepancy report is sent to the NRB.
- The business is not found in the NRB. A proof of registration document (screenshot of the registration or copy of the confirmation email) is required to be uploaded.

### Uploading proof of registration (Custom accounts) 

In the case the business is not found in the NRB, a screenshot of the beneficial owner information from the NRB must be uploaded using the [identity.business_details.documents.proof_of_registration.files](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-proof_of_registration-files) parameter. Set the `File`’s `purpose` parameter to `account_requirement`.:

#### curl

```bash
curl https://files.stripe.com/v1/files \
  -u <<YOUR_SECRET_KEY>>: \
  -H "Stripe-Account: {{CONNECTED_STRIPE_ACCOUNT_ID}}" \
  -F "purpose"="account_requirement" \
  -F "file"="@/path/to/a/file"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

Stripe::File.create({
  purpose: 'account_requirement',
  file: File.new('/path/to/a/file.jpg'),
}, {
  stripe_account: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
})
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

with open("/path/to/a/file.jpg", "rb") as fp:
  stripe.File.create(
    purpose='account_requirement',
    file=fp,
    stripe_account='{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  )
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

\Stripe\File::create([
  'purpose' => 'account_requirement',
  'file' => fopen('/path/to/a/file.jpg', 'r'),
], [
  'stripe_account' => '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
]);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

FileCreateParams params =
  FileCreateParams.builder()
    .setPurpose(FileCreateParams.Purpose.account_requirement)
    .setFile(new java.io.File("/path/to/a/file.jpg"))
    .build();

RequestOptions requestOptions =
  RequestOptions.builder()
    .setStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
    .build();

File file = File.create(params);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const fs = require('fs');
const file = await stripe.files.create({
  purpose: 'account_requirement',
  file: {
    data: fs.readFileSync('/path/to/a/file.jpg'),
    name: 'file_name.jpg',
    type: 'application/octet-stream',
  },
}, {
  stripeAccount: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
});
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

fp, _ := os.Open("/path/to/a/success.png")
params := &stripe.FileParams{
  FileReader: fp,
  Filename: stripe.String("success.png"),
  Purpose: stripe.String(string(stripe.FileUploadPurposeAccountRequirement)),
}
params.SetStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
f, _ := file.New(params)
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var filename = "/path/to/a/success.png";
using (FileStream stream = System.IO.File.Open(filename, FileMode.Open))
{
  var options = new FileCreateOptions
  {
    File = stream,
    Purpose = FilePurpose.AccountRequirement,
  };
  var service = new FileService();
  var upload = service.Create(options);
}
```

This request uploads the file and returns a token:

```json
{
  "id": "file_5dtoJkOhAxrMWb",
  "created": 1403047735,
  "size": 4908
}
```

You can then use the token’s `id` value to attach the file to a connected account for identity verification.

```curl
curl -X POST https://api.stripe.com/v2/core/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -H "Authorization: Bearer <<YOUR_SECRET_KEY>>" \
  -H "Stripe-Version: preview" \
  --json '{
    "include": [
        "identity"
    ],
    "identity": {
        "business_details": {
            "documents": {
                "proof_of_registration": {
                    "files": [
                        "file_5dtoJkOhAxrMWb"
                    ]
                }
            }
        }
    }
  }'
```

```cli
stripe v2 core accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  --include=identity \
  --identity.business-details.documents.proof-of-registration.files=file_5dtoJkOhAxrMWb
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      business_details: {
        documents: {proof_of_registration: {files: ['file_5dtoJkOhAxrMWb']}},
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "include": ["identity"],
    "identity": {
      "business_details": {
        "documents": {"proof_of_registration": {"files": ["file_5dtoJkOhAxrMWb"]}},
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->v2->core->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'include' => ['identity'],
    'identity' => [
      'business_details' => [
        'documents' => ['proof_of_registration' => ['files' => ['file_5dtoJkOhAxrMWb']]],
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
    .addInclude(AccountUpdateParams.Include.IDENTITY)
    .setIdentity(
      AccountUpdateParams.Identity.builder()
        .setBusinessDetails(
          AccountUpdateParams.Identity.BusinessDetails.builder()
            .setDocuments(
              AccountUpdateParams.Identity.BusinessDetails.Documents.builder()
                .setProofOfRegistration(
                  AccountUpdateParams.Identity.BusinessDetails.Documents.ProofOfRegistration.builder()
                    .addFile("file_5dtoJkOhAxrMWb")
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

Account account =
  client.v2().core().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      business_details: {
        documents: {
          proof_of_registration: {
            files: ['file_5dtoJkOhAxrMWb'],
          },
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.V2CoreAccountUpdateParams{
  Include: []*string{stripe.String("identity")},
  Identity: &stripe.V2CoreAccountUpdateIdentityParams{
    BusinessDetails: &stripe.V2CoreAccountUpdateIdentityBusinessDetailsParams{
      Documents: &stripe.V2CoreAccountUpdateIdentityBusinessDetailsDocumentsParams{
        ProofOfRegistration: &stripe.V2CoreAccountUpdateIdentityBusinessDetailsDocumentsProofOfRegistrationParams{
          Files: []*string{stripe.String("file_5dtoJkOhAxrMWb")},
        },
      },
    },
  },
}
result, err := sc.V2CoreAccounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.V2.Core.AccountUpdateOptions
{
    Include = new List<string> { "identity" },
    Identity = new Stripe.V2.Core.AccountUpdateIdentityOptions
    {
        BusinessDetails = new Stripe.V2.Core.AccountUpdateIdentityBusinessDetailsOptions
        {
            Documents = new Stripe.V2.Core.AccountUpdateIdentityBusinessDetailsDocumentsOptions
            {
                ProofOfRegistration = new Stripe.V2.Core.AccountUpdateIdentityBusinessDetailsDocumentsProofOfRegistrationOptions
                {
                    Files = new List<string> { "file_5dtoJkOhAxrMWb" },
                },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V2.Core.Accounts;
Stripe.V2.Core.Account account = service.Update(
    "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
    options);
```

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [identity.attestations.ownership_declaration](https://docs.stripe.com/api/v2/core/accounts/update.md?lang=curl&api-version=preview#v2_update_accounts-identity-attestations-ownership_declaration) hash.

```curl
curl -X POST https://api.stripe.com/v2/core/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -H "Authorization: Bearer <<YOUR_SECRET_KEY>>" \
  -H "Stripe-Version: preview" \
  --json '{
    "include": [
        "identity"
    ],
    "identity": {
        "attestations": {
            "ownership_declaration": {
                "date": "2022-09-18T13:22:18.123Z",
                "ip": "8.8.8.8",
                "user_agent": "Mozilla/5.0"
            }
        }
    }
  }'
```

```cli
stripe v2 core accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  --include=identity \
  --identity.attestations.ownership-declaration.date="2022-09-18T13:22:18.123Z" \
  --identity.attestations.ownership-declaration.ip="8.8.8.8" \
  --identity.attestations.ownership-declaration.user-agent="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

account = client.v2.core.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "include": ["identity"],
    "identity": {
      "attestations": {
        "ownership_declaration": {
          "date": "2022-09-18T13:22:18.123Z",
          "ip": "8.8.8.8",
          "user_agent": "Mozilla/5.0",
        },
      },
    },
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->v2->core->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'include' => ['identity'],
    'identity' => [
      'attestations' => [
        'ownership_declaration' => [
          'date' => '2022-09-18T13:22:18.123Z',
          'ip' => '8.8.8.8',
          'user_agent' => 'Mozilla/5.0',
        ],
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
    .addInclude(AccountUpdateParams.Include.IDENTITY)
    .setIdentity(
      AccountUpdateParams.Identity.builder()
        .setAttestations(
          AccountUpdateParams.Identity.Attestations.builder()
            .setOwnershipDeclaration(
              AccountUpdateParams.Identity.Attestations.OwnershipDeclaration.builder()
                .setDate(Instant.parse("2022-09-18T13:22:18.123Z"))
                .setIp("8.8.8.8")
                .setUserAgent("Mozilla/5.0")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

Account account =
  client.v2().core().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.v2.core.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    include: ['identity'],
    identity: {
      attestations: {
        ownership_declaration: {
          date: '2022-09-18T13:22:18.123Z',
          ip: '8.8.8.8',
          user_agent: 'Mozilla/5.0',
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.V2CoreAccountUpdateParams{
  Include: []*string{stripe.String("identity")},
  Identity: &stripe.V2CoreAccountUpdateIdentityParams{
    Attestations: &stripe.V2CoreAccountUpdateIdentityAttestationsParams{
      OwnershipDeclaration: &stripe.V2CoreAccountUpdateIdentityAttestationsOwnershipDeclarationParams{
        Date: stripe.Time(time.Now()),
        IP: stripe.String("8.8.8.8"),
        UserAgent: stripe.String("Mozilla/5.0"),
      },
    },
  },
}
result, err := sc.V2CoreAccounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.V2.Core.AccountUpdateOptions
{
    Include = new List<string> { "identity" },
    Identity = new Stripe.V2.Core.AccountUpdateIdentityOptions
    {
        Attestations = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOptions
        {
            OwnershipDeclaration = new Stripe.V2.Core.AccountUpdateIdentityAttestationsOwnershipDeclarationOptions
            {
                Date = DateTimeOffset.Parse("2022-09-18T13:22:18.123Z").UtcDateTime,
                Ip = "8.8.8.8",
                UserAgent = "Mozilla/5.0",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V2.Core.Accounts;
Stripe.V2.Core.Account account = service.Update(
    "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
    options);
```

#### Business representative verification

Stripe requires the account representative to undergo enhanced identity verification and address verification.

##### Enhanced identity verification

Singapore requires using [Singpass MyInfo](https://www.singpass.gov.sg/main/individuals/) for enhanced identity verification of representatives for all business types. If users don’t have access to MyInfo, they must verify liveness using [Stripe Identity](https://docs.stripe.com/identity.md).

Successful completion of enhanced identity verification using either SingPass MyInfo or Stripe Identity requires integrating with Connect Onboarding or Embedded Onboarding. If you’re using the Stripe API to onboard connected accounts, you must update your forms to collect the new required verification information from your users, and redirect them to Connect Onboarding at the final stage to complete the enhanced identity verification.

##### Address verification

Verification of the business representative’s address is required for all businesses. If Stripe can’t verify the address, you must collect a [proof of address document](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=address).

#### Proof of authority verification 

Stripe needs to verify that the [account representative has sufficient authority](https://support.stripe.com/questions/representative-authority-verification) to open an account on behalf of the legal entity.

If we can’t verify this programmatically, Stripe returns the [verification_failed_representative_authority](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-requirements-entries-errors) error code. If possible, change the representative to a person with sufficient authority.

If necessary, you can allow a business representative without verifiable authority by having a person with authority authorize them in writing:

1. Add the person with authority as a `Person` with the [authorizer relationship](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-authorizer). You must provide their first name, last name, and identity document.
1. Have the person with authority sign a Letter of Authorization that permits the business representative to manage the account. They must create the letter using [this template](https://b.stripecdn.com/content/Letter_of_authorization_for_Stripe_Singapore.pdf).
1. Provide the signed letter as the [documents.company_authorization](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-documents-company_authorization) on the `Person` representing the business representative.

Additionally, the Letter of Authorization requirement can expose document-related errors such as `verification_document_name_mismatch`, or `verification_document_type_not_supported`. Make sure you can handle [Document Verification Errors](https://docs.stripe.com/connect/handling-api-verification.md#handle-document-verification-problems) and the [New verification error codes](https://docs.stripe.com/connect/required-verification-information.md#new-verification-error-codes).

#### Proof of authority verification 

Stripe needs to verify that the [account representative has sufficient authority](https://support.stripe.com/questions/representative-authority-verification) to open an account on behalf of the legal entity.

If we can’t verify this programmatically, Stripe returns the [verification_failed_representative_authority](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-requirements-entries-errors) error code. If possible, change the representative to a person with sufficient authority.

If necessary, you can allow a business representative without verifiable authority by having a person with authority authorize them in writing:

1. Add the person with authority as a `Person` with the [authorizer relationship](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-authorizer). You must provide their first name, last name, and identity document.
1. Have the person with authority sign a Letter of Authorization that permits the business representative to manage the account. They must create the letter using [this template](https://b.stripecdn.com/content/Letter_of_authorization_for_Stripe_Singapore.pdf).
1. Provide the signed letter as the [documents.company_authorization](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-documents-company_authorization) on the `Person` representing the business representative.

Additionally, the Letter of Authorization requirement can expose document-related errors such as `verification_document_name_mismatch`, or `verification_document_type_not_supported`. Make sure you can handle [Document Verification Errors](https://docs.stripe.com/connect/handling-api-verification.md#handle-document-verification-problems) and the [New verification error codes](https://docs.stripe.com/connect/required-verification-information.md#new-verification-error-codes).

#### Legal entity verification 

Stripe requires the verification of business name, UEN, and legal entity type. If we can’t verify the existence of the company, you must collect a company document.

Stripe also needs to check that the business type and business structure matches the local government records. When a mismatch in [identity.entity_type](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-entity_type) or [identity.business_details.structure](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-business_details-structure) occurs, it generates an error, and you need to update the information or provide a company document to verify the legal entity.

UEN information might be verified with the data made available at https://data.gov.sg under the terms of the [Singapore Open Data License version 1.0](https://data.gov.sg/open-data-licence).

#### Legal entity verification 

Stripe requires the verification of business name, UEN, and legal entity type. If we can’t verify the existence of the company, you must collect a company document.

Stripe also needs to check that the business type and business structure matches the local government records. When a mismatch in [identity.entity_type](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-entity_type) or [identity.business_details.structure](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-business_details-structure) occurs, it generates an error, and you need to update the information or provide a company document to verify the legal entity.

UEN information might be verified with the data made available at https://data.gov.sg under the terms of the [Singapore Open Data License version 1.0](https://data.gov.sg/open-data-licence).

#### Ultimate beneficial ownership verification 

The ultimate beneficial ownership verification requirements depend on the user’s business type.

##### Private Companies

Stripe defines and attempts to identify any individual with 25% or more ownership as the ultimate beneficial owner (UBO). We recommend using [Stripe-hosted](https://docs.stripe.com/connect/hosted-onboarding.md) or [Embedded Onboarding](https://docs.stripe.com/connect/embedded-onboarding.md) to allow your users to preview and confirm the owners. Alternatively, you can collect and add all UBOs to the account as `Persons` with the [owner relationship](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner).

If Stripe can’t determine these individuals, a company must submit a [proof of ultimate beneficial ownership document](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) to attest to their ownership structure. This must include proof of ownership documents for any holding companies owning 25% or more shares of the connected account. Stripe-hosted or embedded onboarding automatically seeks to collect these documents, or you can collect and submit them using the Accounts API. You must add all UBOs listed on the proof of ultimate beneficial ownership to the account.

> Connected accounts can submit a single [ultimate beneficial owner attestation](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) as an alternative to one document for the business and another for each holding company with significant ownership.
> 
> If the company has no owners with 25% or more ownership, all directors listed on government records (and available for preview on Stripe-hosted or embedded onboarding) are considered to be the UBOs and you must add them to the account.

##### Partnerships

Partnerships must verify the relationship between the business and all partners, managers, and any other individual with 25% or more ownership. You must add all such people to the account as `Persons` with the [owner relationship](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner).

##### Exemptions

In certain situations, a business entity might not need to declare its ownership. To qualify for an exemption, you must provide a legitimate reason in the [identity.attestations.persons_provided.ownership_exemption_reason](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-attestations-persons_provided-ownership_exemption_reason) field. Valid reasons for exemption include:

- `qualified_entity_exceeds_ownership_threshold`: If a government, publicly listed company, or financial institution owns at least 75% of the business, the business is exempt from providing ownership details.
- `qualifies_as_financial_institution`: Businesses that are financial institutions regulated by the [Monetary Authority of Singapore](https://eservices.mas.gov.sg/fid/institution?sector=Banking&category=Finance%20Company) are exempt from sharing ownership details.

After submitting an exemption reason, we’ll review the business entity’s details. If Stripe determines that the business entity doesn’t qualify for the exemption, we display an error message, and the account must declare its ownership by providing an [identity.business_details.documents.proof_of_ultimate_beneficial_ownership](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-identity-business_details-documents-proof_of_ultimate_beneficial_ownership).

##### Integration details  

You must add UBOs and directors to the account with the [owner](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-owner) or [director](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-director) position in the API. Private companies, private partnerships, and non-profits require proof of beneficial ownership. When we can’t successfully verify the UBO, you need to collect an [ID Document](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) for the unverified UBO.

If Stripe determines that the account is missing owners, directors, or documentation of holding companies, its `requirements` hash includes an entry with the [description](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-requirements-entries-description) `identity.business_details.documents.proof_of_ultimate_beneficial_ownership`. You can find a complete list of acceptable documents at [Acceptable Verification Documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship).

Additionally, you could be asked to attest that the list of directors is current and correct by setting the following fields in the API:

- `identity.attestations.directorship_declaration.ip`
- `identity.attestations.directorship_declaration.date`
- (Optional) `identity.attestations.directorship_declaration.user_agent`

If a discrepancy in your list of directors is detected, Stripe might request a new declaration by returning one or more of those properties as entries in the `requirements` hash.

Both Stripe-hosted and embedded onboarding display a list of missing owners and directors, and the account user can add them to their account by clicking on them. Adding the suggested individuals fulfills the UBO requirement for companies without any holding companies in their ownership structure. For companies with holding companies, Stripe attempts to verify their owners. If we can’t, we prompt the account user to upload either an [ultimate beneficial owner attestation document or relevant ownership documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) to determine the account’s ultimate beneficial owners. (This also applies to other business types, such as non-profits.)

Accounts with missing persons have one or more of the following error codes in the `errors` hash of the requirement:

- Accounts with missing beneficial owners: `verification_missing_owners`
- Accounts with missing directors: `verification_missing_directors`
- Accounts where Stripe requires additional information regarding their ownership: `verification_requires_additional_proof_of_registration`

#### Ultimate beneficial ownership verification 

In the case of non-profits, all key executives and directors are considered UBOs. This includes:

- President
- Director
- CEO
- Treasurer
- Secretary or General Secretary
- Chairman
- Trustee
- Newly added positions
- And any of these positions in an Assistant, Deputy, or Vice capacity.

Stripe attempts to identify all directors and key executives of charities registered in Singapore, which you can preview and confirm in Stripe-hosted or embedded onboarding. All other non-profits must provide a [proof of ultimate beneficial ownership document](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) and you must add the listed individuals to the account with the [director](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-director) position in the API.

##### Integration details 

You must add all UBOs of non-profits to the account with [director](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-relationship-director) position in the API. When we can’t successfully verify the UBO, you need to collect an [ID Document](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) for the unverified UBO.

If Stripe determines that the account is missing owners, directors, or documentation of holding companies, its `requirements` hash will include an entry with the [description](https://docs.stripe.com/api/v2/core/accounts/object.md?api-version=preview#v2_account_object-requirements-entries-description) `identity.business_details.documents.proof_of_ultimate_beneficial_ownership`. You can find a complete list of acceptable documents for Singapore at [Acceptable Verification Documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship).

Both Stripe-hosted and embedded onboarding display a list of missing owners and directors, and the account user can add them to their account by clicking on them. Adding the suggested individuals fulfills the UBO requirement for companies without any holding companies in their ownership structure. For companies with holding companies, Stripe attempts to verify their owners. If we can’t, we prompt the account user to upload either [an ultimate beneficial owner attestation document or relevant ownership documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) to determine the account’s ultimate beneficial owners. (This also applies to other business types, such as non-profits.)

Non-profits with missing beneficial owners have a `verification_missing_directors` error code in the `errors` hash of the requirement.

#### Closure of unverified accounts

As required under [Singapore’s Payment Services Act](https://stripe.com/guides/sg-payment-services-act-2019), we’re permanently closing Singapore accounts that remain unverified for over 120 business days. These are accounts whose charges or payouts have already been suspended, so this closure affects only inactive accounts.

To help you identify affected accounts, we upload monthly reports titled “Unverified account list” to your Dashboard under the [Compliance and documents section](https://dashboard.stripe.com/settings/documents), where you can find the list of impacted accounts and their requirement deadlines. Any accounts closed in the last month are in the report titled “Closed unverified account list.”

We’ll close any account that hasn’t been verified by its designated deadline. The account owner needs to provide the missing verification information before the deadline to keep the account open. If the information is provided after the deadline has passed, we’ll release any remaining balance to the account holder’s bank account, but we won’t be able to reactivate their Stripe account.

Stripe sends emails to Standard and Express accounts that remain unverified for too long, to inform them of the impending closure and to remind them to update their account details. Stripe won’t communicate with Custom connected accounts directly. That means you, as the platform, can contact them to avoid account closures.

#### Additional identity verification

To comply with regulatory requirements in Thailand, we require additional identity verification for certain connected accounts. This entails taking a selfie and uploading a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) using [Stripe Identity](https://docs.stripe.com/identity.md). Your platform needs to integrate with [Connect Onboarding](https://stripe.com/connect/onboarding) to satisfy this identity verification requirement.

Additional identity verification applies to the representatives and beneficial owners of connected accounts belonging to individuals, sole proprietors and unregistered partnerships.

> If you don’t integrate with [Connect Onboarding](https://stripe.com/connect/onboarding), you won’t be able to onboard connected accounts subject to additional identity verification.

#### Registered address requirement

The registered address requirement refers to the Household Registration address. Please provide an address as per the ‘Tabien Bann’ or Household Registration book, also known as the Blue book for Thai nationals, or Yellow book for non-Thai nationals. To collect a Household Registration address, add an entry to the `Person`’s [additional_addresses](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-additional_addresses) array, and set its `purpose` parameter to `registered`.

If the user is neither a Thai national nor resident of Thailand, collect their current residential address with the same parameter instead.

#### ID number requirement

The ID number requirement refers to the 13-digit code found on the front of a Thai ID card, and secondary ID number requirement refers to the laser code found at the back of a Thai ID card. Add both of these numbers to the `Person`’s [id_numbers](https://docs.stripe.com/api/v2/core/persons/object.md?api-version=preview#v2_person_object-id_numbers) array. For the Thai ID number, set its `type` parameter to `th_pin`, and for the laser code, set its `type` parameter to `th_lc`.

These requirements are only applicable to Thai nationals, so leave the parameters empty if the user isn’t a Thai national.

#### Additional information on the individual 

If Stripe can’t verify the individual, or if they’re not a Thai national, you need to collect a scan of a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). To collect a government-issued ID document, add its front and back images to the `Account`’s [identity.individual.documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-documents-primary_verification) hash.

#### Additional information on the representative 

If Stripe can’t verify the account representative, or if they’re not a Thai national, you need to provide a scan of a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). To collect a government-issued ID document, add its front and back images to the `Person`’s [documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) hash.

#### Additional information on beneficial owners

Accounts belonging to companies and registered partnerships are required to provide information on all beneficial owners. A beneficial owner is defined as any individual who owns 25% or more shares of the business. If there is no such person, then any individual who exercises significant control over the company is considered a beneficial owner. Otherwise, please provide information on any individual holding the position of senior management.

If Stripe can’t verify a beneficial owner, or if they’re not a Thai national, you need to provide a scan of a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). To collect a government-issued ID document, add its front and back images to the `Person`’s [documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) hash.

#### Additional information on the company

If Stripe can’t verify the company, you need to provide a scan of a [company verification document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) issued less than 6 months ago. To collect a company verification document, add its front and back images to the `Account`’s [identity.business_details.documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-primary_verification) hash.

#### Additional tax information

If Stripe can’t verify the company’s EIN (Employer Identification Number), upload a copy of [an IRS Letter 147C document or an IRS SS-4 confirmation letter](https://support.stripe.com/questions/using-irs-documentation-as-reference-when-entering-business-name-and-tax-id-number-tin-for-us-based-businesses) using the [identity.business_details.documents.company_tax_id_verification](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-documents-company_tax_id_verification) parameter. The document must include the connected account’s company name and EIN.

#### Additional information on the account 

Enabling card payments requires a validated city, state, and postal code for `identity.business_details.address`. It also requires the company’s EIN (Employer Identification Number) within 30 days or before 1,500 USD in payments, whichever comes first. If Stripe can’t verify the EIN before the deadline, we disable card payments.

Enabling *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit) requires a validated full `identity.business_details.address` and EIN within 30 days. Stripe disables payouts if we haven’t verified the full address or the EIN before the deadline.

#### Additional information on the individual 

Enabling card payments requires a validated city, state, and ZIP code for `identity.individual.address`.

Enabling payouts requires a validated full `identity.individual.address`. Stripe disables payouts if we haven’t verified the full address within 30 days.

If the individual fails verification with their `us_ssn_last_4`, then enabling card payments requires verifying their full SSN and their identity. Upload their full SSN using the [identity.individual.id_numbers](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-id_numbers) parameter.

#### Additional information on the representative 

A [Person](https://docs.stripe.com/api/v2/core/accounts/create-person.md?api-version=preview) known as a representative must activate this connected account. The representative must have significant responsibility to control, manage, or direct the organization, and must be authorized by the organization to agree to Stripe’s terms. The representative must be either [an owner or an executive](https://support.stripe.com/questions/beneficial-owner-and-director-definitions), which you specify by setting either [relationship.owner](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-relationship-owner) or [relationship.executive](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-relationship-executive) to true.

If the representative fails verification with their `us_ssn_last_4`, then enabling card payments requires verifying their full SSN and their identity. Upload their full SSN using the [id_numbers](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-id_numbers) parameter.

If Stripe can’t verify the representative, or if the person doesn’t have an SSN, then enabling card payments requires you to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). Upload the front and back of the document using the [documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) parameter.

#### Additional information for minors

If the account representative is a minor, you must verify the minor’s legal guardian. A legal guardian is a [Person](https://docs.stripe.com/api/v2/core/accounts/create-person.md?api-version=preview) object on the account with [relationship.legal_guardian](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-relationship-legal_guardian) set to `true`. Additionally, the legal guardian must provide their information and sign the Stripe terms of service, which we store on the `Person` object that represents the guardian. Store the legal guardian’s terms of service acceptance in the [additional_terms_of_service](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-additional_terms_of_service) hash.

If the legal guardian fails verification with `us_ssn_last_4`, then enabling card payments requires verifying their full SSN and their identity. Upload their full SSN using the [id_numbers](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-id_numbers) parameter.

If Stripe can’t verify the legal guardian, or if the person doesn’t have an SSN, then enabling card payments requires you to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). Upload the front and back of the document using the [documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) parameter.

#### Additional information on owners 

You must collect information on all [owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who have 25% or more ownership of the company, represented by [Person](https://docs.stripe.com/api/v2/core/persons.md?api-version=preview) objects on the account. When you finish collecting the required owner information, you must set [identity.attestations.persons_provided.owners](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-attestations-persons_provided-owners) to true.

If the company has any unverified 25% owners, Stripe can disable payouts on the connected account when it reaches 750,000 USD in charges. To avoid disabling payouts, you must collect the following information for all 25% owners:

- [address](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-address)
- [date_of_birth.day](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-date_of_birth-day)
- [date_of_birth.month](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-date_of_birth-month)
- [date_of_birth.year](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-date_of_birth-year)
- [id_numbers](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-id_numbers) (`us_ssn_last_4`)
- [phone](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-phone)

If Stripe can’t verify an owner, or if an owner doesn’t have an SSN, you must provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) for them. Upload the front and back of the document using the [documents.primary_verification](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-documents-primary_verification) parameter.

Optionally, you can collect company ownership information by setting [relationship.owner](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-relationship-owner) to true and [relationship.percent_ownership](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-relationship-percent_ownership) to the person’s ownership percentage. If you don’t specify `relationship.percent_ownership`, it defaults to 25%.

#### Supported business structures 

Optionally, you can provide information on the [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) of your connected account’s business using the [identity.business_details.structure](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-structure) parameter.

Stripe supports the following business structures for privately held companies:

- `multi_member_llc`
- `single_member_llc`
- `private_partnership`
- `private_corporation`
- `unincorporated_association`

Stripe supports the following business structures for publicly traded companies. If you assign either of these structures to the business, then the [representative](https://docs.stripe.com/connect/required-verification-information.md#additional-company-card-representative-us) doesn’t need to be an owner or an executive, and you don’t need to provide information on additional owners.

- `public_corporation`
- `public_partnership`

#### Supported business structures 

Optionally, you can provide information on the [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) of your connected account’s business using the [identity.business_details.structure](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-structure) parameter.

Stripe supports the following business structures for government entities:

- `governmental_unit`
- `government_instrumentality`

If your connected account is an instrumentality with tax-exempt status, you can set the `identity.business_details.structure` to `tax_exempt_government_instrumentality`.

#### Supported business structures 

While uncommon, there are circumstances where an `individual` business operates and is treated more like a `company`, such as a single-member LLC. For these accounts, you can optionally provide information on their [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) using the [identity.business_details.structure](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-structure) parameter.

If your connected account’s business has only one member or owner, and is registered as an LLC with a US state, you can set the [identity.entity_type](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-entity_type) to `company` and the `identity.business_details.structure` to `single_member_llc`. You collect the same required information, except you use the [identity.business_details](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details) hash and the [Persons v2](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview) API instead of the `identity.individual` hash. In that case, you map requirements in the `identity.individual` hash to the corresponding properties of the account’s representative. For example, set `given_name` on the representative’s `Person` instead of `identity.individual.given_name` on the `Account`.

If your connected account has a business identification (for example, a tax ID separate from their personal ID, or a business address different than their home address), you can set the `identity.entity_type` to `company` and the `identity.business_details.structure` to `sole_proprietorship`. You collect the same required information, except you use the [identity.business_details](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details) hash and the [Persons v2](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview) API instead of the `individual` hash In that case, you map requirements in the `individual` hash to the corresponding properties of the account’s representative. For example, set `given_name` on the representative’s `Person` instead of `identity.individual.given_name` on the `Account`.

#### Supported business structures 

Optionally, you can provide information on the [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) of your connected account’s business using the [identity.business_details.structure](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-structure) parameter.

Stripe supports the following business structures for non-profit organizations:

- `incorporated_non_profit`
- `unincorporated_non_profit`

The US government grants tax-exempt status to certain government entities that it considers to be non-profit. If your connected account is an instrumentality with tax-exempt status, you can set the [identity.entity_type](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-entity_type) to `government_entity` and the `identity.business_details.structure` to `tax_exempt_government_instrumentality`. You must then collect the [appropriate verification requirements](https://docs.stripe.com/connect/required-verification-information.md#government-entity-card-us).

#### Tax reporting information

Not applicable to Accounts v2.

#### Threshold information 

In addition to the onboarding requirements, payouts require that you provide your connected account’s Employer Identification Number (EIN) before its charges reach a certain threshold. The threshold is either 10,000 USD or 3,000 USD, depending on your industry and Stripe’s review of your platform profile. Provide the EIN using the [identity.business_details.id_numbers](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-business_details-id_numbers) parameter. If Stripe can’t verify your connected account’s EIN before it reaches the threshold, we disable payouts.

#### Threshold information 

In addition to the onboarding requirements, payouts require that you provide the following information for your connected account before its charges reach a certain threshold. The threshold is either 10,000 USD or 3,000 USD, depending on your industry and Stripe’s review of your platform profile. If Stripe can’t verify this information before the account reaches the threshold, we disable payouts.

- [identity.individual.date_of_birth.day](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-date_of_birth-day)
- [identity.individual.date_of_birth.month](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-date_of_birth--month)
- [identity.individual.date_of_birth.year](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-date_of_birth-year)
- [identity.individual.id_numbers](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-id_numbers) (`us_ssn_last_4`)

#### Payments threshold information

When a connected account with `card_payments` reaches 500,000 USD in lifetime charges, we will collect the last four digits of their SSN or ITIN for US-resident owners and attempt to programmatically obtain the full nine digit SSN for them. If we are unable to obtain the full number using information provided, the User will need to provide the full nine digits. If the full number has already been provided, it is not needed again.

For connected accounts with `identity.entity_type` set to `individual`, and where the owner isn’t a minor, add the SSN as type `us_ssn` using the `Account`’s [identity.individual.id_numbers](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-identity-individual-id_numbers) parameter. For other accounts, which have associated persons with `relationship.legal_guardian`, `relationship.representative`, or `relationship.owner` set to true, add the SSN as type `us_ssn` using the appropriate `Person`’s [id_numbers](https://docs.stripe.com/api/v2/core/accounts/update-person.md?api-version=preview#v2_update_persons-id_numbers) parameter.

#### Support phone number

For accounts without access to a Stripe-hosted dashboard, and where Stripe is responsible for negative balances, activating `card_payments` requires [configuration.merchant.support.phone](https://docs.stripe.com/api/v2/core/accounts/update.md?api-version=preview#v2_update_accounts-configuration-merchant-support-phone).

#### Additional information on the account 

If Stripe can’t verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using:

- [company.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-front)
- [company.verification.document.back](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-back)

#### Additional information on the individual 

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the individual or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns).

Collect ID information using:

- [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front)
- [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back)

#### Additional information on the representative 

A person known as a representative must activate this connected account. This person must be a [beneficial owner](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who is authorized to sign for the company. Indicate this relationship to Stripe by setting [relationship.executive](https://docs.stripe.com/api/persons/object.md#person_object-relationship-executive) to true, or, if the representative owns 25% or more of the company, by setting [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) to true.

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the representative or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

Optionally, you can collect the representative’s ownership information using [relationship.representative](https://docs.stripe.com/api/persons/object.md#person_object-relationship-representative) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership).

#### Additional information on directors 

For companies (excluding partnerships), you must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Directors are members of the governing board of the company. When you have finished collecting the required information from all directors, or if your company doesn’t have any directors, you must inform Stripe by setting [company.directors_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-directors_provided) to true.

If there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

#### Additional information on beneficial owners (for both executives and owners) 

You must collect information on all [beneficial owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Beneficial owners are persons who exercise significant management control over the company (executives) or who own 25% or more of the company (owners). When you have finished collecting the required information from all beneficial owners, you must inform Stripe by setting both [company.owners_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-owners_provided) and [company.executives_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-executives_provided) to true.

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify a beneficial owner or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). Collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

Optionally, you can collect ownership information on each person who owns 25% or more of the company using [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership).

#### Additional information on the account 

If Stripe can’t verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=entity#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using the [company.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-front) and [company.verification.document.back](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-back) arguments.

#### Additional information on the representative 

If Stripe can’t verify the representative, they need to provide proof of liveness, which entails taking a selfie and uploading a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=identity#acceptable-verification-documents) using [Stripe Identity](https://docs.stripe.com/identity.md). Your platform needs to integrate with [Connect Onboarding](https://stripe.com/connect/onboarding) to satisfy this requirement.

Alternatively, you can provide a scan of a government-issued ID document and a scan of an address document. To collect a government-issued ID document, use the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters. To collect an address document, use the [verification.additional_document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-front) and [verification.additional_document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-back) parameters. You can’t provide the same document for both identity and address verification.

#### Additional information on the individual 

Individuals that Stripe can’t verify must provide proof of liveness, which entails taking a selfie and uploading a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=identity#acceptable-verification-documents) using [Stripe Identity](https://docs.stripe.com/identity.md). Your platform needs to integrate with [Connect Onboarding](https://stripe.com/connect/onboarding) to allow such individuals to complete this requirement.

Alternatively, your platform can collect scans of an individual’s ID and address documents and [upload them](https://docs.stripe.com/api/files/create.md) to Stripe. After uploading, submit the individual’s ID document with the [individual.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/object.md#account_object-individual-verification-document-back) fields and the address document with the [individual.verification.additional_document.front](https://docs.stripe.com/api/accounts/object.md#account_object-individual-verification-additional_document-front) and [individual.verification.additional_document.back](https://docs.stripe.com/api/accounts/object.md#account_object-individual-verification-additional_document-back) fields. You can’t provide the same document for both identity and address verification.

#### Additional information on owners 

You must collect information on all [owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Owners are any individual who owns 25% or more of the company. When you finish collecting the required information from all owners, set [company.owners_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-owners_provided) to true. This lets Stripe know that you have completed this requirement.

Optionally, you can collect ownership information on each person who owns 25% or more of the company with [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership).

#### Additional information on directors 

You must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). We check the [director](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) information you supply against the registry and results in one of these outcomes:

- The business is found in the registry, and the information matches. The account fully onboards, and requires no additional action.
- The business is found in the registry, but the director information doesn’t match. You must upload a [proof of registration document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=relationship#acceptable-verification-documents) using the [documents.proof_of_registration.files](https://docs.stripe.com/api/accounts/create.md#create_account-documents-proof_of_registration-files) parameter. Set the `File`’s `purpose` parameter to `account_requirement`.

#### curl

```bash
curl https://files.stripe.com/v1/files \
  -u <<YOUR_SECRET_KEY>>: \
  -H "Stripe-Account: {{CONNECTED_STRIPE_ACCOUNT_ID}}" \
  -F "purpose"="account_requirement" \
  -F "file"="@/path/to/a/file"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

Stripe::File.create({
  purpose: 'account_requirement',
  file: File.new('/path/to/a/file.jpg'),
}, {
  stripe_account: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
})
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

with open("/path/to/a/file.jpg", "rb") as fp:
  stripe.File.create(
    purpose='account_requirement',
    file=fp,
    stripe_account='{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  )
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

\Stripe\File::create([
  'purpose' => 'account_requirement',
  'file' => fopen('/path/to/a/file.jpg', 'r'),
], [
  'stripe_account' => '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
]);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

FileCreateParams params =
  FileCreateParams.builder()
    .setPurpose(FileCreateParams.Purpose.account_requirement)
    .setFile(new java.io.File("/path/to/a/file.jpg"))
    .build();

RequestOptions requestOptions =
  RequestOptions.builder()
    .setStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
    .build();

File file = File.create(params);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const fs = require('fs');
const file = await stripe.files.create({
  purpose: 'account_requirement',
  file: {
    data: fs.readFileSync('/path/to/a/file.jpg'),
    name: 'file_name.jpg',
    type: 'application/octet-stream',
  },
}, {
  stripeAccount: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
});
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

fp, _ := os.Open("/path/to/a/success.png")
params := &stripe.FileParams{
  FileReader: fp,
  Filename: stripe.String("success.png"),
  Purpose: stripe.String(string(stripe.FileUploadPurposeAccountRequirement)),
}
params.SetStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
f, _ := file.New(params)
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var filename = "/path/to/a/success.png";
using (FileStream stream = System.IO.File.Open(filename, FileMode.Open))
{
  var options = new FileCreateOptions
  {
    File = stream,
    Purpose = FilePurpose.AccountRequirement,
  };
  var service = new FileService();
  var upload = service.Create(options);
}
```

This request uploads the file and returns a token:

```json
{
  "id": "file_5dtoJkOhAxrMWb",
  "created": 1403047735,
  "size": 4908
}
```

You can then use the token’s `id` value to attach the file to a connected account for identity verification.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "documents[proof_of_registration][files][]"=file_5dtoJkOhAxrMWb
```

```cli
stripe accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -d "documents[proof_of_registration][files][0]"=file_5dtoJkOhAxrMWb
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {documents: {proof_of_registration: {files: ['file_5dtoJkOhAxrMWb']}}},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {"documents": {"proof_of_registration": {"files": ["file_5dtoJkOhAxrMWb"]}}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  ['documents' => ['proof_of_registration' => ['files' => ['file_5dtoJkOhAxrMWb']]]]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountUpdateParams params =
  AccountUpdateParams.builder()
    .setDocuments(
      AccountUpdateParams.Documents.builder()
        .setProofOfRegistration(
          AccountUpdateParams.Documents.ProofOfRegistration.builder()
            .addFile("file_5dtoJkOhAxrMWb")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    documents: {
      proof_of_registration: {
        files: ['file_5dtoJkOhAxrMWb'],
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
  Documents: &stripe.AccountUpdateDocumentsParams{
    ProofOfRegistration: &stripe.AccountUpdateDocumentsProofOfRegistrationParams{
      Files: []*string{stripe.String("file_5dtoJkOhAxrMWb")},
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Documents = new AccountDocumentsOptions
    {
        ProofOfRegistration = new AccountDocumentsProofOfRegistrationOptions
        {
            Files = new List<string> { "file_5dtoJkOhAxrMWb" },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", options);
```

#### Additional information on registration statuses

If Stripe can’t verify the registration status of the charity, you need to collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md?country=CA&document-type=entity#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Upload it using the [documents.company_registration_verification.files](https://docs.stripe.com/api/accounts/update.md#update_account-documents-company_registration_verification-files) parameter.

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [company.ownership_declaration](https://docs.stripe.com/api/accounts/object.md#account_object-company-ownership_declaration) hash.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  --data-urlencode "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```cli
stripe accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  -d "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "company": {
      "ownership_declaration": {
        "date": 1609798905,
        "ip": "8.8.8.8",
        "user_agent": "Mozilla/5.0",
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
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'company' => [
      'ownership_declaration' => [
        'date' => 1609798905,
        'ip' => '8.8.8.8',
        'user_agent' => 'Mozilla/5.0',
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
        .setOwnershipDeclaration(
          AccountUpdateParams.Company.OwnershipDeclaration.builder()
            .setDate(1609798905L)
            .setIp("8.8.8.8")
            .setUserAgent("Mozilla/5.0")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
    OwnershipDeclaration: &stripe.AccountUpdateCompanyOwnershipDeclarationParams{
      Date: stripe.Int64(1609798905),
      IP: stripe.String("8.8.8.8"),
      UserAgent: stripe.String("Mozilla/5.0"),
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Company = new AccountCompanyOptions
    {
        OwnershipDeclaration = new AccountCompanyOwnershipDeclarationOptions
        {
            Date = DateTimeOffset.FromUnixTimeSeconds(1609798905).UtcDateTime,
            Ip = "8.8.8.8",
            UserAgent = "Mozilla/5.0",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", options);
```

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [company.ownership_declaration](https://docs.stripe.com/api/accounts/object.md#account_object-company-ownership_declaration) hash.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  --data-urlencode "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```cli
stripe accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  -d "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "company": {
      "ownership_declaration": {
        "date": 1609798905,
        "ip": "8.8.8.8",
        "user_agent": "Mozilla/5.0",
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
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'company' => [
      'ownership_declaration' => [
        'date' => 1609798905,
        'ip' => '8.8.8.8',
        'user_agent' => 'Mozilla/5.0',
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
        .setOwnershipDeclaration(
          AccountUpdateParams.Company.OwnershipDeclaration.builder()
            .setDate(1609798905L)
            .setIp("8.8.8.8")
            .setUserAgent("Mozilla/5.0")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
    OwnershipDeclaration: &stripe.AccountUpdateCompanyOwnershipDeclarationParams{
      Date: stripe.Int64(1609798905),
      IP: stripe.String("8.8.8.8"),
      UserAgent: stripe.String("Mozilla/5.0"),
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Company = new AccountCompanyOptions
    {
        OwnershipDeclaration = new AccountCompanyOwnershipDeclarationOptions
        {
            Date = DateTimeOffset.FromUnixTimeSeconds(1609798905).UtcDateTime,
            Ip = "8.8.8.8",
            UserAgent = "Mozilla/5.0",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", options);
```

#### Additional information on the account 

If Stripe can’t verify the company, or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns), you must collect a [proof-of-entity document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit). Collect it using the [company.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-company-verification-document-front) and [company.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-company-verification-document-back) parameters.

#### Additional information on the individual 

Depending on the situation, you might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents), an address document, or both to enable payouts. That can happen if Stripe can’t verify the individual or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

Collect ID information using the [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back) parameters, and address information using the [verification.additional_document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-front) and [verification.additional_document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-back) parameters.

#### Additional information on the representative 

A person known as a representative must activate this connected account. The representative must be a [beneficial owner](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who is authorized to sign for the company. Indicate this relationship to Stripe by setting [relationship.executive](https://docs.stripe.com/api/persons/object.md#person_object-relationship-executive) to true, or, if the representative owns 25% or more of the company, by setting [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) to true.

You might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) and an [address document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the representative’s provided information or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

Additionally, for partnerships you must provide a [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership) value.

You can collect ID information with the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

#### Additional information on directors 

For companies, excluding partnerships, you must collect information on all [directors](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Directors are members of the governing board of the company. When you have finished collecting the required information from all directors, or if your company doesn’t have any directors, you must inform Stripe by setting [company.directors_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-directors_provided) to true.

You might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) and an [address document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the director’s provided information or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

You can collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters, and address information using the [verification.additional_document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-front) and [verification.additional_document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-back) parameters.

#### Additional information on beneficial owners (for both executives and owners) 

You must collect information on all [beneficial owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions). Beneficial owners are persons who exercise significant management control over the company (executives) or who own 25% or more of the company (owners). When you have finished collecting the required information from all beneficial owners, you must inform Stripe by setting both [company.owners_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-owners_provided) and [company.executives_provided](https://docs.stripe.com/api/accounts/object.md#account_object-company-executives_provided) to true.

You might need to collect a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) and an [address document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) to enable payouts. That can happen if Stripe can’t verify the beneficial owner’s provided information or if there are possible concerns about [sanctions](https://docs.stripe.com/connect/risk-management/best-practices.md#sanctions-concerns). In some cases, depending on various calculated risk factors, Stripe can use Simplified Due Diligence and request only one document for verification at a later time.

You can collect ID information using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters, and address information using the [verification.additional_document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-front) and [verification.additional_document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-additional_document-back) parameters.

Optionally, you can collect ownership information on each person who owns 25% or more of the company with [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership).

Additionally, for partnerships you need to provide a [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership) value for any owners added to the account.

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [company.ownership_declaration](https://docs.stripe.com/api/accounts/object.md#account_object-company-ownership_declaration) hash.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  --data-urlencode "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```cli
stripe accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  -d "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "company": {
      "ownership_declaration": {
        "date": 1609798905,
        "ip": "8.8.8.8",
        "user_agent": "Mozilla/5.0",
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
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'company' => [
      'ownership_declaration' => [
        'date' => 1609798905,
        'ip' => '8.8.8.8',
        'user_agent' => 'Mozilla/5.0',
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
        .setOwnershipDeclaration(
          AccountUpdateParams.Company.OwnershipDeclaration.builder()
            .setDate(1609798905L)
            .setIp("8.8.8.8")
            .setUserAgent("Mozilla/5.0")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
    OwnershipDeclaration: &stripe.AccountUpdateCompanyOwnershipDeclarationParams{
      Date: stripe.Int64(1609798905),
      IP: stripe.String("8.8.8.8"),
      UserAgent: stripe.String("Mozilla/5.0"),
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Company = new AccountCompanyOptions
    {
        OwnershipDeclaration = new AccountCompanyOwnershipDeclarationOptions
        {
            Date = DateTimeOffset.FromUnixTimeSeconds(1609798905).UtcDateTime,
            Ip = "8.8.8.8",
            UserAgent = "Mozilla/5.0",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", options);
```

### Universal Beneficial Ownership Verification 

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, no additional action is required. A discrepancy report is sent to the NRB.
- The business is not found in the NRB. A proof of registration document (screenshot of the registration or copy of the confirmation email) is required to be uploaded.

### Uploading proof of registration (Custom accounts) 

In the case the business is not found in the NRB, a screenshot of the beneficial owner information from the NRB must be uploaded using the [documents.proof_of_registration.files](https://docs.stripe.com/api/accounts/create.md#create_account-documents-proof_of_registration-files) parameter. Set the `File`’s `purpose` parameter to `account_requirement`.:

#### curl

```bash
curl https://files.stripe.com/v1/files \
  -u <<YOUR_SECRET_KEY>>: \
  -H "Stripe-Account: {{CONNECTED_STRIPE_ACCOUNT_ID}}" \
  -F "purpose"="account_requirement" \
  -F "file"="@/path/to/a/file"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

Stripe::File.create({
  purpose: 'account_requirement',
  file: File.new('/path/to/a/file.jpg'),
}, {
  stripe_account: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
})
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

with open("/path/to/a/file.jpg", "rb") as fp:
  stripe.File.create(
    purpose='account_requirement',
    file=fp,
    stripe_account='{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  )
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

\Stripe\File::create([
  'purpose' => 'account_requirement',
  'file' => fopen('/path/to/a/file.jpg', 'r'),
], [
  'stripe_account' => '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
]);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

FileCreateParams params =
  FileCreateParams.builder()
    .setPurpose(FileCreateParams.Purpose.account_requirement)
    .setFile(new java.io.File("/path/to/a/file.jpg"))
    .build();

RequestOptions requestOptions =
  RequestOptions.builder()
    .setStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
    .build();

File file = File.create(params);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const fs = require('fs');
const file = await stripe.files.create({
  purpose: 'account_requirement',
  file: {
    data: fs.readFileSync('/path/to/a/file.jpg'),
    name: 'file_name.jpg',
    type: 'application/octet-stream',
  },
}, {
  stripeAccount: '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
});
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

fp, _ := os.Open("/path/to/a/success.png")
params := &stripe.FileParams{
  FileReader: fp,
  Filename: stripe.String("success.png"),
  Purpose: stripe.String(string(stripe.FileUploadPurposeAccountRequirement)),
}
params.SetStripeAccount("{{CONNECTED_STRIPE_ACCOUNT_ID}}")
f, _ := file.New(params)
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var filename = "/path/to/a/success.png";
using (FileStream stream = System.IO.File.Open(filename, FileMode.Open))
{
  var options = new FileCreateOptions
  {
    File = stream,
    Purpose = FilePurpose.AccountRequirement,
  };
  var service = new FileService();
  var upload = service.Create(options);
}
```

This request uploads the file and returns a token:

```json
{
  "id": "file_5dtoJkOhAxrMWb",
  "created": 1403047735,
  "size": 4908
}
```

You can then use the token’s `id` value to attach the file to a connected account for identity verification.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "documents[proof_of_registration][files][]"=file_5dtoJkOhAxrMWb
```

```cli
stripe accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -d "documents[proof_of_registration][files][0]"=file_5dtoJkOhAxrMWb
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {documents: {proof_of_registration: {files: ['file_5dtoJkOhAxrMWb']}}},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.update(
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {"documents": {"proof_of_registration": {"files": ["file_5dtoJkOhAxrMWb"]}}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  ['documents' => ['proof_of_registration' => ['files' => ['file_5dtoJkOhAxrMWb']]]]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountUpdateParams params =
  AccountUpdateParams.builder()
    .setDocuments(
      AccountUpdateParams.Documents.builder()
        .setProofOfRegistration(
          AccountUpdateParams.Documents.ProofOfRegistration.builder()
            .addFile("file_5dtoJkOhAxrMWb")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    documents: {
      proof_of_registration: {
        files: ['file_5dtoJkOhAxrMWb'],
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
  Documents: &stripe.AccountUpdateDocumentsParams{
    ProofOfRegistration: &stripe.AccountUpdateDocumentsProofOfRegistrationParams{
      Files: []*string{stripe.String("file_5dtoJkOhAxrMWb")},
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Documents = new AccountDocumentsOptions
    {
        ProofOfRegistration = new AccountDocumentsProofOfRegistrationOptions
        {
            Files = new List<string> { "file_5dtoJkOhAxrMWb" },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", options);
```

### Universal Beneficial Ownership Verification

After supplying the beneficial owner information, it is checked against the National Registry of Businesses (NRB). Depending on the results of this check, there are three outcomes:

- The business is found in the NRB, and the information matches. The account is fully onboarded, and no additional action is required.
- The business is found in the NRB, but the information doesn’t match. The account is fully onboarded, and no additional action is required. A discrepancy report is sent to the NRB.
- The business isn’t found in the NRB. An attestation must be provided, declaring that the business is registered with the NRB and that the information given to Stripe matches.

In the case where the business isn’t found in the NRB, provide the attestation by setting the `date`, `ip_address`, and `user_agent` in the `Account`’s [company.ownership_declaration](https://docs.stripe.com/api/accounts/object.md#account_object-company-ownership_declaration) hash.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  --data-urlencode "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```cli
stripe accounts update {{CONNECTED_STRIPE_ACCOUNT_ID}} \
  -d "company[ownership_declaration][date]"=1609798905 \
  -d "company[ownership_declaration][ip]"="8.8.8.8" \
  -d "company[ownership_declaration][user_agent]"="Mozilla/5.0"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
  "{{CONNECTED_STRIPE_ACCOUNT_ID}}",
  {
    "company": {
      "ownership_declaration": {
        "date": 1609798905,
        "ip": "8.8.8.8",
        "user_agent": "Mozilla/5.0",
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
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  [
    'company' => [
      'ownership_declaration' => [
        'date' => 1609798905,
        'ip' => '8.8.8.8',
        'user_agent' => 'Mozilla/5.0',
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
        .setOwnershipDeclaration(
          AccountUpdateParams.Company.OwnershipDeclaration.builder()
            .setDate(1609798905L)
            .setIp("8.8.8.8")
            .setUserAgent("Mozilla/5.0")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_STRIPE_ACCOUNT_ID}}',
  {
    company: {
      ownership_declaration: {
        date: 1609798905,
        ip: '8.8.8.8',
        user_agent: 'Mozilla/5.0',
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
    OwnershipDeclaration: &stripe.AccountUpdateCompanyOwnershipDeclarationParams{
      Date: stripe.Int64(1609798905),
      IP: stripe.String("8.8.8.8"),
      UserAgent: stripe.String("Mozilla/5.0"),
    },
  },
}
result, err := sc.V1Accounts.Update(
  context.TODO(), "{{CONNECTED_STRIPE_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Company = new AccountCompanyOptions
    {
        OwnershipDeclaration = new AccountCompanyOwnershipDeclarationOptions
        {
            Date = DateTimeOffset.FromUnixTimeSeconds(1609798905).UtcDateTime,
            Ip = "8.8.8.8",
            UserAgent = "Mozilla/5.0",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_STRIPE_ACCOUNT_ID}}", options);
```

#### Business representative verification

Stripe requires the account representative to undergo enhanced identity verification and address verification.

##### Enhanced identity verification

Singapore requires using [Singpass MyInfo](https://www.singpass.gov.sg/main/individuals/) for enhanced identity verification of representatives for all business types. If users don’t have access to MyInfo, they must verify liveness using [Stripe Identity](https://docs.stripe.com/identity.md).

Successful completion of enhanced identity verification using either SingPass MyInfo or Stripe Identity requires integrating with Connect Onboarding or Embedded Onboarding. If you’re using the Stripe API to onboard connected accounts, you must update your forms to collect the new required verification information from your users, and redirect them to Connect Onboarding at the final stage to complete the enhanced identity verification.

##### Address verification

Verification of the business representative’s address is required for all businesses. If Stripe can’t verify the address, you must collect a [proof of address document](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=address).

#### Proof of authority verification 

Stripe needs to verify that the [account representative has sufficient authority](https://support.stripe.com/questions/representative-authority-verification) to open an account on behalf of the legal entity.

If we can’t verify this programmatically, Stripe returns the [verification_failed_representative_authority](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-errors-code) error code. If possible, change the representative to a person with sufficient authority.

If necessary, you can allow a business representative without verifiable authority by having a person with authority authorize them in writing:

1. Add the person with authority as a `Person` with the [authorizer relationship](https://docs.stripe.com/api/persons/update.md#update_person-relationship-authorizer). You must provide their first name, last name, and identity document.
1. Have the person with authority sign a Letter of Authorization that permits the business representative to manage the account. They must create the letter using [this template](https://b.stripecdn.com/content/Letter_of_authorization_for_Stripe_Singapore.pdf).
1. Provide the signed letter as the [documents.company_authorization](https://docs.stripe.com/api/persons/update.md#update_person-documents-company_authorization) on the `Person` representing the business representative.

Additionally, the Letter of Authorization requirement can expose document-related errors such as `verification_document_name_mismatch`, or `verification_document_type_not_supported`. Make sure you can handle [Document Verification Errors](https://docs.stripe.com/connect/handling-api-verification.md#handle-document-verification-problems) and the [New verification error codes](https://docs.stripe.com/connect/required-verification-information.md#new-verification-error-codes).

#### Proof of authority verification 

Stripe needs to verify that the [account representative has sufficient authority](https://support.stripe.com/questions/representative-authority-verification) to open an account on behalf of the legal entity.

If we can’t verify this programmatically, Stripe returns the [verification_failed_representative_authority](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-errors-code) error code. If possible, change the representative to a person with sufficient authority.

If necessary, you can allow a business representative without verifyable authority by having a person with authority authorize them in writing:

1. Add the person with authority as a `Person` with the [authorizer relationship](https://docs.stripe.com/api/persons/update.md#update_person-relationship-authorizer). You must provide their first name, last name, and identity document.
1. Have the person with authority sign a Letter of Authorization that permits the business representative to manage the account. They must create the letter using [this template](https://b.stripecdn.com/content/Letter_of_authorization_for_Stripe_Singapore.pdf).
1. Provide the signed letter as the [documents.company_authorization](https://docs.stripe.com/api/persons/update.md#update_person-documents-company_authorization) on the `Person` representing the business representative.

Additionally, the Letter of Authorization requirement can expose document-related errors such as `verification_document_name_mismatch`, or `verification_document_type_not_supported`. Make sure you can handle [Document Verification Errors](https://docs.stripe.com/connect/handling-api-verification.md#handle-document-verification-problems) and the [New verification error codes](https://docs.stripe.com/connect/required-verification-information.md#new-verification-error-codes).

#### Legal entity verification 

Stripe requires the verification of business name, UEN, and legal entity type. If we can’t verify the existence of the company, you must collect a company document.

Stripe also needs to check that the business type and business structure matches the local government records. When a mismatch in [business type](https://docs.stripe.com/api/accounts/object.md#account_object-business_type) or [business structure](https://docs.stripe.com/api/accounts/object.md#account_object-company-structure) occurs, it generates a `verification_legal_entity_structure_mismatch` error, and you need to update the business type or structure, or provide a company document to verify the legal entity.

UEN information might be verified with the data made available at https://data.gov.sg under the terms of the [Singapore Open Data License version 1.0](https://data.gov.sg/open-data-licence).

#### Legal entity verification 

Stripe requires the verification of business name, UEN, and legal entity type. If we can’t verify the existence of the company, you must collect a company document.

Stripe also needs to check that the business type and business structure matches the local government records. When a mismatch in [business type](https://docs.stripe.com/api/accounts/object.md#account_object-business_type) or [business structure](https://docs.stripe.com/api/accounts/object.md#account_object-company-structure) occurs, it generates a `verification_legal_entity_structure_mismatch` error, and you need to update the business type or structure, or provide a company document to verify the legal entity.

UEN information might be verified with the data made available at https://data.gov.sg under the terms of the [Singapore Open Data License version 1.0](https://data.gov.sg/open-data-licence).

#### Ultimate beneficial ownership verification 

The ultimate beneficial ownership verification requirements depend on the user’s business type.

##### Private Companies

Stripe defines and attempts to identify any individual with 25% or more ownership as the ultimate beneficial owner (UBO). We recommend using [Stripe-hosted](https://docs.stripe.com/connect/hosted-onboarding.md) or [Embedded Onboarding](https://docs.stripe.com/connect/embedded-onboarding.md) to allow your users to preview and confirm the owners. Alternatively, you can collect and add all UBOs to the account as `Persons` with the [owner relationship](https://docs.stripe.com/api/persons/update.md#update_person-relationship-owner).

If Stripe can’t determine these individuals, a company must submit a [proof of ultimate beneficial ownership document](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) to attest to their ownership structure. This must include proof of ownership documents for any holding companies owning 25% or more shares of the connected account. Stripe-hosted or embedded onboarding automatically seeks to collect these documents, or you can collect and submit them using the Accounts API. You must add all UBOs listed on the proof of ultimate beneficial ownership to the account.

> Connected accounts can submit a single [ultimate beneficial owner attestation](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) as an alternative to one document for the business and another for each holding company with significant ownership.
> 
> If the company has no owners with 25% or more ownership, all directors listed on government records (and available for preview on Stripe-hosted or embedded onboarding) are considered to be the UBOs and you must add them to the account.

##### Partnerships

Partnerships must verify the relationship between the business and all partners, managers, and any other individual with 25% or more ownership. You must add all such people to the account as `Persons` with the [owner relationship](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner).

##### Exemptions

In certain situations, a business entity might not need to declare its ownership. To qualify for an exemption, you must provide a legitimate reason in the [company.ownership_exemption_reason](https://docs.stripe.com/api/accounts/update.md#update_account-company-ownership_exemption_reason) field. Valid reasons for exemption include:

- `qualified_entity_exceeds_ownership_threshold`: If a government, publicly listed company, or financial institution owns at least 75% of the business, the business is exempt from providing ownership details.
- `qualifies_as_financial_institution`: Businesses that are financial institutions regulated by the [Monetary Authority of Singapore](https://eservices.mas.gov.sg/fid/institution?sector=Banking&category=Finance%20Company) are exempt from sharing ownership details.

After submitting an exemption reason, we review the business entity’s details. During this review, the requirement moves to [requirements.pending_verification](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-pending_verification). If Stripe determines that the business entity doesn’t qualify for the exemption, we display an error message, and the ownership requirement remains:

```
{
  "id": ""{{CONNECTED_ACCOUNT_ID}}"",
  "requirements": {
    "past_due": [
      "documents.proof_of_ultimate_beneficial_ownership.files",
    ],
    "alternatives": [
      {
        "original_fields_due": [
          "documents.proof_of_ultimate_beneficial_ownership.files",
        ],
        "alternative_fields_due": [
          "company.ownership_exemption_reason",
        ]
      }
    ],
    "errors": [
      {
        "code": "verification_rejected_ownership_exemption_reason",
        "reason": "The ownership exemption reason was rejected.",
        "requirement": "company.ownership_exemption_reason"
      }
    ],
    ...
  },
  ...
}
```

##### Integration details  

You must add UBOs and directors to the account with the [owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) or [director](https://docs.stripe.com/api/persons/object.md#person_object-relationship-director) position in the API. Private companies, private partnerships, and non-profits require proof of beneficial ownership. When we can’t successfully verify the UBO, you need to collect an [ID Document](https://docs.stripe.com/api/persons/update.md#update_person-verification-document) for the unverified UBO.

If Stripe determines that the account is missing owners, directors, or documentation of holding companies, the `documents.proof_of_ultimate_beneficial_ownership.files` field will be returned in [requirements](https://docs.stripe.com/api/accounts/object.md#account_object-requirements). You can find a complete list of acceptable documents at [Acceptable Verification Documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship).

Additionally, you could be asked to attest that the list of directors is current and correct by setting the following fields in the API:

- `company.directorship_declaration.ip`
- `company.directorship_declaration.date`
- (Optional) `company.directorship_declaration.user_agent`

If a discrepancy in your list of directors is detected, Stripe might request a new declaration by returning the `company.directorship_declaration.ip` and `company.directorship_declaration.date` requirements in the requirements field.

Both Stripe-hosted and embedded onboarding display a list of missing [owners](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [directors](https://docs.stripe.com/api/persons/object.md#person_object-relationship-director), and the account user can add them to their account by clicking on them. Adding the suggested individuals fulfills the UBO requirement for companies without any holding companies in their ownership structure. For companies with holding companies, Stripe attempts to verify their owners. If we can’t, we prompt the account user to upload either an [ultimate beneficial owner attestation document or relevant ownership documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) to determine the account’s ultimate beneficial owners. (This also applies to other business types, such as non-profits.)

Accounts with missing beneficial owners have a `verification_missing_owners` error code in the errors hash of [requirements](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-errors). Similarly, accounts with missing directors have a `verification_document_directors_mismatch` error code. Lastly, accounts where Stripe requires additional information regarding their ownership have a `verification_requires_additional_proof_of_registration` error code.

```
{
  "id": ""{{CONNECTED_ACCOUNT_ID}}"",
  "requirements": {
    "past_due": [
      "documents.proof_of_ultimate_beneficial_ownership.files",
    ],
    "errors": [
      {
        "code": "verification_missing_owners",
        "requirement": "documents.proof_of_ultimate_beneficial_ownership.files",
        "reason": "..."
      },
      ...
    ],
    ...
  },
  ...
}
```

#### Ultimate beneficial ownership verification 

In the case of non-profits, all key executives and directors are considered UBOs. This includes:

- President
- Director
- CEO
- Treasurer
- Secretary or General Secretary
- Chairman
- Trustee
- Newly added positions
- And any of these positions in an Assistant, Deputy, or Vice capacity.

Stripe attempts to identify all directors and key executives of charities registered in Singapore, which you can preview and confirm in Stripe-hosted or embedded onboarding. All other non-profits must provide a [proof of ultimate beneficial ownership document](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) and you must add the listed individuals to the account with the [director](https://docs.stripe.com/api/persons/object.md#person_object-relationship-director) position in the API.

##### Integration details 

You must add all UBOs of non-profits to the account with [director](https://docs.stripe.com/api/persons/object.md#person_object-relationship-director) position in the API. When we can’t successfully verify the UBO, you need to collect an [ID Document](https://docs.stripe.com/api/persons/update.md#update_person-verification-document) for the unverified UBO.

If Stripe determines that the account is missing owners, directors, or documentation of holding companies, the `documents.proof_of_ultimate_beneficial_ownership.files` field [becomes due](https://docs.stripe.com/api/accounts/object.md#account_object-requirements). You can find a complete list of acceptable documents for Singapore at [Acceptable Verification Documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship).

Both Stripe-hosted and embedded onboarding display a list of missing [owners](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) and [directors](https://docs.stripe.com/api/persons/object.md#person_object-relationship-director), and the account user can add them to their account by clicking on them. Adding the suggested individuals fulfills the UBO requirement for companies without any holding companies in their ownership structure. For companies with holding companies, Stripe attempts to verify its owners. If we can’t, we prompt the account user to upload either [an ultimate beneficial owner attestation document or relevant ownership documents](https://docs.stripe.com/acceptable-verification-documents.md?country=SG&document-type=relationship) to determine the account’s ultimate beneficial owners. (This also applies to other business types, such as non-profits.)

Non-profits with missing beneficial owners have a `verification_document_directors_mismatch` error code.

```
{
  "id": ""{{CONNECTED_ACCOUNT_ID}}"",
  "requirements": {
    "past_due": [
      "documents.proof_of_ultimate_beneficial_ownership.files",
    ],
    "errors": [
      {
        "code": "verification_document_directors_mismatch",
        "requirement": "documents.proof_of_ultimate_beneficial_ownership.files",
        "reason": "..."
      },
      ...
    ],
    ...
  },
  ...
}
```

#### Closure of unverified accounts

As required under [Singapore’s Payment Services Act](https://stripe.com/guides/sg-payment-services-act-2019), we’re permanently closing Singapore accounts that remain unverified for over 120 business days. These are accounts whose charges or payouts have already been suspended, so this closure affects only inactive accounts.

To help you identify affected accounts, we upload monthly reports titled “Unverified account list” to your Dashboard under the [Compliance and documents section](https://dashboard.stripe.com/settings/documents), where you can find the list of impacted accounts and their requirement deadlines. Any accounts closed in the last month are in the report titled “Closed unverified account list.”

We’ll close any account that hasn’t been verified by its designated deadline. The account owner needs to provide the missing verification information before the deadline to keep the account open. If the information is provided after the deadline has passed, we’ll release any remaining balance to the account holder’s bank account, but we won’t be able to reactivate their Stripe account.

Stripe sends emails to Standard and Express accounts that remain unverified for too long, to inform them of the impending closure and to remind them to update their account details. Stripe won’t communicate with Custom connected accounts directly. That means you, as the platform, can contact them to avoid account closures.

Accounts that are closed under this process have their [disabled_reason](https://docs.stripe.com/api/accounts/object.md#account_object-requirements-disabled_reason) set to `rejected.other`.

#### Additional identity verification

To comply with regulatory requirements in Thailand, we require additional identity verification for certain connected accounts. This entails taking a selfie and uploading a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) using [Stripe Identity](https://docs.stripe.com/identity.md). Your platform needs to integrate with [Connect Onboarding](https://stripe.com/connect/onboarding) to satisfy this identity verification requirement.

Additional identity verification applies to the representatives and beneficial owners of connected accounts belonging to individuals, sole proprietors and unregistered partnerships.

> If you don’t integrate with [Connect Onboarding](https://stripe.com/connect/onboarding), you won’t be able to onboard connected accounts subject to additional identity verification.

#### Registered address requirement

The registered address requirement refers to the Household Registration address. Please provide an address as per the ‘Tabien Bann’ or Household Registration book, also known as the Blue book for Thai nationals, or Yellow book for non-Thai nationals. To collect a Household Registration address, use the `Person`’s [registered_address](https://docs.stripe.com/api/persons/object.md#person_object-registered_address) parameter.

If the user is neither a Thai national nor resident of Thailand, collect their current residential address with the same parameter instead.

#### ID number requirement

The ID number requirement refers to the 13-digit code found on the front of a Thai ID card, and secondary ID number requirement refers to the laser code found at the back of a Thai ID card. To collect a Thai ID number, use the `Person`’s [id_number](https://docs.stripe.com/api/persons/create.md#create_person-id_number) parameter, and to collect a laser code use the [id_number_secondary](https://docs.stripe.com/api/persons/create.md#create_person-id_number_secondary) parameter.

These requirements are only applicable to Thai nationals, so leave the parameters empty if the user isn’t a Thai national.

#### Additional information on the individual 

If Stripe can’t verify the individual, or if they’re not a Thai national, you need to collect a scan of a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). To collect a government-issued ID document, use the `Account`’s [individual.verification.document.front](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-front) and [individual.verification.document.back](https://docs.stripe.com/api/accounts/update.md#update_account-individual-verification-document-back) parameters.

#### Additional information on the representative 

If Stripe can’t verify the account representative, or if they’re not a Thai national, you need to provide a scan of a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). To collect a government-issued ID document, use the `Person`’s [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

#### Additional information on beneficial owners

Accounts belonging to companies and registered partnerships are required to provide information on all beneficial owners. A beneficial owner is defined as any individual who owns 25% or more shares of the business. If there is no such person, then any individual who exercises significant control over the company is considered a beneficial owner. Otherwise, please provide information on any individual holding the position of senior management.

If Stripe can’t verify a beneficial owner, or if they’re not a Thai national, you need to provide a scan of a [government-issued ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). To collect a government-issued ID document, use the `Person`’s [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

#### Additional information on the company

If Stripe can’t verify the company, you need to provide a scan of a [company verification document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) issued less than 6 months ago. To collect a company verification document scan, use the `Account`’s [company.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-front) and [company.verification.document.back](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-back) parameters.

#### Additional tax information

If Stripe can’t verify the `company.tax_id`, upload a copy of [an IRS Letter 147C document or an IRS SS-4 confirmation letter](https://support.stripe.com/questions/using-irs-documentation-as-reference-when-entering-business-name-and-tax-id-number-tin-for-us-based-businesses) using the [company.verification.document.front](https://docs.stripe.com/api/accounts/object.md#account_object-company-verification-document-front) parameter. The document must include the connected account’s `company.name` and `company.tax_id`.

#### Additional information on the account 

Enabling card payments requires a validated city, state, and postal code for `company.address`. It also requires the `company.tax_id` (EIN) within 30 days or before 1,500 USD in payments, whichever comes first. If Stripe can’t verify the EIN before the deadline, we disable card payments.

Enabling *payouts* (A payout is the transfer of funds to an external account, usually a bank account, in the form of a deposit), requires a validated full `company.address` and `company.tax_id` (EIN) within 30 days. Stripe disables payouts if we haven’t verified the full address or the `company.tax_id` before the deadline.

#### Additional information on the individual 

Enabling card payments requires a validated city, state, and ZIP code for `individual.address`.

Enabling payouts requires a validated full `individual.address`. Stripe disables payouts if we haven’t verified the full address within 30 days.

If the individual fails verification with their `ssn_last_4`, then enabling card payments requires verifying their full SSN and their identity. Upload their full SSN using the [individual.id_number](https://docs.stripe.com/api/accounts/update.md#update_account-individual-id_number) parameter.

#### Additional information on the representative 

A [Person](https://docs.stripe.com/api/persons.md) known as a representative must activate this connected account. The representative must have significant responsibility to control, manage, or direct the organization, and must be authorized by the organization to agree to Stripe’s terms. The representative must be either [an owner or an executive](https://support.stripe.com/questions/beneficial-owner-and-director-definitions), which you specify by setting [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) or [relationship.executive](https://docs.stripe.com/api/persons/object.md#person_object-relationship-executive) to true.

If the representative fails verification with `ssn_last_4`, then enabling card payments requires verifying their full SSN and their identity. Upload their full SSN using the [person.id_number](https://docs.stripe.com/api/persons/update.md#update_person-id_number) parameter.

If Stripe can’t verify the representative, or if the person doesn’t have an SSN, then enabling card payments requires you to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). Upload the front and back of the document using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

#### Additional information for minors

If the account representative is a minor, you must verify the minor’s legal guardian. A legal guardian is a [Person](https://docs.stripe.com/api/persons.md) object on the account with [relationship.legal_guardian](https://docs.stripe.com/api/persons/create.md#create_person-relationship-legal_guardian) set to `true`. Additionally, the legal guardian must provide their information and sign the Stripe terms of service, which we store on the `Person` object that represents the guardian. Store the legal guardian’s terms of service acceptance in the [additional_tos_acceptances](https://docs.stripe.com/api/persons/create.md#create_person-additional_tos_acceptances) hash.

If the legal guardian fails verification with their `ssn_last_4`, then enabling card payments requires verifying their full SSN and their identity. Upload their full SSN using the [person.id_number](https://docs.stripe.com/api/persons/update.md#update_person-id_number) parameter.

If Stripe can’t verify the legal guardian, or if the person doesn’t have an SSN, then enabling card payments requires you to provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents). Upload the front and back of the document using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

#### Additional information on owners 

You must collect information on all [owners](https://support.stripe.com/questions/beneficial-owner-and-director-definitions) who have 25% or more ownership of the company, represented by [Person](https://docs.stripe.com/api/persons.md) objects on the account. When you finish collecting the required owner information, you must set [company.owners_provided](https://docs.stripe.com/api/accounts/update.md#update_account-company-owners_provided) to true.

If the company has any unverified 25% owners, Stripe can disable payouts on the connected account when it reaches 750,000 USD in charges. To avoid disabling payouts, you must collect the following information for all 25% owners:

- [address](https://docs.stripe.com/api/persons/update.md#update_person-address)
- [dob.day](https://docs.stripe.com/api/persons/update.md#update_person-dob-day)
- [dob.month](https://docs.stripe.com/api/persons/update.md#update_person-dob-month)
- [dob.year](https://docs.stripe.com/api/persons/update.md#update_person-dob-year)
- [id_number](https://docs.stripe.com/api/persons/update.md#update_person-id_number)
- [phone](https://docs.stripe.com/api/persons/update.md#update_person-phone)

If Stripe can’t verify an owner, or if an owner doesn’t have an SSN, you must provide a scan of an [ID document](https://docs.stripe.com/connect/handling-api-verification.md#acceptable-verification-documents) for them. Upload the front and back of the document using the [verification.document.front](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-front) and [verification.document.back](https://docs.stripe.com/api/persons/object.md#person_object-verification-document-back) parameters.

Optionally, you can collect company ownership information by setting [relationship.owner](https://docs.stripe.com/api/persons/object.md#person_object-relationship-owner) to true and [relationship.percent_ownership](https://docs.stripe.com/api/persons/object.md#person_object-relationship-percent_ownership) to the person’s ownership percentage. If you don’t specify `relationship.percent_ownership`, it defaults to 25%.

#### Supported business structures 

Optionally, you can provide information on the [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) of your connected account’s business using the [company.structure](https://docs.stripe.com/api/accounts/create.md#create_account-company-structure) parameter.

Stripe supports the following business structures for privately held companies:

- `multi_member_llc`
- `single_member_llc`
- `private_partnership`
- `private_corporation`
- `unincorporated_association`

Stripe supports the following business structures for publicly traded companies. If you assign either of these structures to the business, then the [representative](https://docs.stripe.com/connect/required-verification-information.md#additional-company-card-representative-us) doesn’t need to be an owner or an executive, and you don’t need to provide information on additional owners.

- `public_corporation`
- `public_partnership`

#### Supported business structures 

Optionally, you can provide information on the [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) of your connected account’s business using the [company.structure](https://docs.stripe.com/api/accounts/create.md#create_account-company-structure) parameter.

Stripe supports the following business structures for government entities:

- `governmental_unit`
- `government_instrumentality`

If your connected account is an instrumentality with tax-exempt status, you can set the `company.structure` to `tax_exempt_government_instrumentality`.

#### Supported business structures 

While uncommon, there are circumstances where an `individual` business operates and is treated more like a `company`, such as a single-member LLC. For these accounts, you can optionally provide information on their [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) using the [company.structure](https://docs.stripe.com/api/accounts/create.md#create_account-company-structure) parameter.

If your connected account’s business has only one member or owner, and is registered as an LLC with a US state, you can set the `business_type` to `company` and the `company.structure` to `single_member_llc`. You collect the same required information, except you use the [company](https://docs.stripe.com/api/accounts/object.md#account_object-company) hash and the [Persons](https://docs.stripe.com/api/persons.md) API instead of the `individual` hash. In that case, you map requirements in the `individual` hash to the corresponding properties of the account’s representative. For example, set `first_name` on the representative’s `Person` instead of `individual.first_name` on the `Account`.

If your connected account has a business identification (for example, a tax ID separate from their personal ID, or a business address different than their home address), you can set the `business_type` to `company` and the `company.structure` to `sole_proprietorship`. You collect the same required information, except you use the [company](https://docs.stripe.com/api/accounts/object.md#account_object-company) hash and the [Persons](https://docs.stripe.com/api/persons.md) API instead of the `individual` hash In that case, you map requirements in the `individual` hash to the corresponding properties of the account’s representative. For example, set `first_name` on the representative’s `Person` instead of `individual.first_name` on the `Account`.

#### Supported business structures 

Optionally, you can provide information on the [legal structure](https://docs.stripe.com/connect/identity-verification.md#business-structure) of your connected account’s business using the [company.structure](https://docs.stripe.com/api/accounts/create.md#create_account-company-structure) parameter.

Stripe supports the following business structures for non-profit organizations:

- `incorporated_non_profit`
- `unincorporated_non_profit`

The US government grants tax-exempt status to certain government entities that it considers to be non-profit. If your connected account is an instrumentality with tax-exempt status, you can set the `business_type` to `government_entity` and the `company.structure` to `tax_exempt_government_instrumentality`. You must then collect the [appropriate verification requirements](https://docs.stripe.com/connect/required-verification-information.md#government-entity-card-us).

#### Tax reporting information

By default, the requirements for `transfers` don’t collect all information at the appropriate thresholds to file IRS Form 1099-K or Form 1099-MISC. If your business has US federal 1099 filing requirements and plans to file these through Stripe, request the [appropriate tax reporting capability](https://docs.stripe.com/connect/account-capabilities.md#tax-reporting) and make sure to collect the [necessary information](https://docs.stripe.com/connect/required-verification-information-taxes.md) from your users.

#### Threshold information 

In addition to the onboarding requirements, payouts require that you provide your connected account’s `company.tax_id` (EIN) before its charges reach a certain threshold. The threshold is either 10,000 USD or 3,000 USD, depending on your industry and Stripe’s review of your platform profile. If Stripe can’t verify your connected account’s EIN before it reaches the threshold, we disable payouts.

#### Threshold information 

In addition to the onboarding requirements, payouts require that you provide the following information for your connected account before its charges reach a certain threshold. The threshold is either 10,000 USD or 3,000 USD, depending on your industry and Stripe’s review of your platform profile. If Stripe can’t verify this information before the account reaches the threshold, we disable payouts.

- `individual.dob.day`
- `individual.dob.month`
- `individual.dob.year`
- `individual.ssn_last_4`

#### Payments threshold information

When a connected account with `card_payments` reaches 500,000 USD in lifetime charges, we will collect the last four digits of their SSN or ITIN for US-resident owners and attempt to programmatically obtain the full nine digit SSN for them. If we are unable to obtain the full number using information provided, the User will need to provide the full nine digits. If the full number has already been provided, it is not needed again.

For connected accounts with `business_type` set to `individual`, and where the owner isn’t a minor, update the `Account`’s [individual.id_number](https://docs.stripe.com/api/accounts/update.md#update_account-individual-id_number). For other accounts, which have persons with `relationship.legal_guardian`, `relationship.representative`, or `relationship.owner` set to true, update the appropriate `Person`’s [id_number](https://docs.stripe.com/api/persons/update.md#update_person-id_number).

#### Support phone number

For accounts without access to a Stripe-hosted dashboard, and where Stripe is responsible for negative balances, activating `card_payments` requires `business_profile.support_phone`.
