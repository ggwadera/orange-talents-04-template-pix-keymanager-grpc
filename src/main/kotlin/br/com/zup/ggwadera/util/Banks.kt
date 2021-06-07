package br.com.zup.ggwadera.util

object Banks {

    private val banks by lazy {
        hashMapOf(
            "00000000" to "Banco do Brasil S.A.",
            "00000208" to "BRB - BANCO DE BRASILIA S.A.",
            "00038121" to "Banco Central do Brasil - Selic",
            "00038166" to "Banco Central do Brasil",
            "00250699" to "AGK CORRETORA DE CAMBIO S.A.",
            "00315557" to "CONFEDERAÇÃO NACIONAL DAS COOPERATIVAS CENTRAIS UNICRED LTDA. - UNICRED DO BRASI",
            "00329598" to "ÍNDIGO INVESTIMENTOS DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA.",
            "00360305" to "CAIXA ECONOMICA FEDERAL",
            "00394460" to "Secretaria do Tesouro Nacional - STN",
            "00416968" to "Banco Inter S.A.",
            "00460065" to "COLUNA S/A DISTRIBUIDORA DE TITULOS E VALORES MOBILIÁRIOS",
            "00517645" to "BANCO RIBEIRAO PRETO S.A.",
            "00556603" to "BANCO BARI DE INVESTIMENTOS E FINANCIAMENTOS S.A.",
            "00558456" to "Banco Cetelem S.A.",
            "00795423" to "Banco Semear S.A.",
            "00806535" to "Planner Corretora de Valores S.A.",
            "00997185" to "Banco B3 S.A.",
            "01023570" to "Banco Rabobank International Brasil S.A.",
            "01027058" to "CIELO S.A.",
            "01073966" to "Cooperativa de Crédito Rural de Abelardo Luz - Sulcredi/Crediluz",
            "01181521" to "BANCO COOPERATIVO SICREDI S.A.",
            "01330387" to "COOPERATIVA DE CRÉDITO RURAL DE PEQUENOS AGRICULTORES E DA REFORMA AGRÁRIA DO CE",
            "01522368" to "Banco BNP Paribas Brasil S.A.",
            "01634601" to "CENTRAL DE COOPERATIVAS DE ECONOMIA E CRÉDITO MÚTUO DO ESTADO DO RIO GRANDE DO S",
            "01658426" to "COOPERFORTE - COOPERATIVA DE ECONOMIA E CRÉDITO MÚTUO DE FUNCIONÁRIOS DE INSTITU",
            "01701201" to "Kirton Bank S.A. - Banco Múltiplo",
            "01800019" to "PORTOCRED S.A. - CREDITO, FINANCIAMENTO E INVESTIMENTO",
            "01852137" to "BBC LEASING S.A. - ARRENDAMENTO MERCANTIL",
            "01858774" to "BANCO BV S.A.",
            "02038232" to "BANCO COOPERATIVO SICOOB S.A. - BANCO SICOOB",
            "02276653" to "TRINUS CAPITAL DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS S.A.",
            "02318507" to "BANCO KEB HANA DO BRASIL S.A.",
            "02332886" to "XP INVESTIMENTOS CORRETORA DE CÂMBIO,TÍTULOS E VALORES MOBILIÁRIOS S/A",
            "02398976" to "UNIPRIME NORTE DO PARANÁ - COOPERATIVA DE CRÉDITO LTDA",
            "02685483" to "CM CAPITAL MARKETS CORRETORA DE CÂMBIO, TÍTULOS E VALORES MOBILIÁRIOS LTDA",
            "02801938" to "BANCO MORGAN STANLEY S.A.",
            "02819125" to "UBS Brasil Corretora de Câmbio, Títulos e Valores Mobiliários S.A.",
            "02992317" to "Treviso Corretora de Câmbio S.A.",
            "02992335" to "Câmara Interbancária de Pagamentos - CIP - LDL",
            "03012230" to "Hipercard Banco Múltiplo S.A.",
            "03017677" to "Banco J. Safra S.A.",
            "03046391" to "UNIPRIME CENTRAL - CENTRAL INTERESTADUAL DE COOPERATIVAS DE CREDITO LTDA.",
            "03215790" to "Banco Toyota do Brasil S.A.",
            "03311443" to "PARATI - CREDITO, FINANCIAMENTO E INVESTIMENTO S.A.",
            "03323840" to "Banco Alfa S.A.",
            "03502968" to "PI Distribuidora de Títulos e Valores Mobiliários S.A.",
            "03532415" to "Banco ABN Amro S.A.",
            "03609817" to "Banco Cargill S.A.",
            "03751794" to "Terra Investimentos Distribuidora de Títulos e Valores Mobiliários Ltda.",
            "03973814" to "SERVICOOP - COOPERATIVA DE CRÉDITO DOS SERVIDORES PÚBLICOS ESTADUAIS DO RIO GRAN",
            "04062902" to "VISION S.A. CORRETORA DE CAMBIO",
            "04184779" to "Banco Bradescard S.A.",
            "04257795" to "Nova Futura Corretora de Títulos e Valores Mobiliários Ltda.",
            "04307598" to "FIDÚCIA SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E À EMPRESA DE PEQUENO PORTE L",
            "04332281" to "GOLDMAN SACHS DO BRASIL BANCO MULTIPLO S.A.",
            "04391007" to "Câmara Interbancária de Pagamentos",
            "04632856" to "Credisis - Central de Cooperativas de Crédito Ltda.",
            "04715685" to "COOPERATIVA DE CRÉDITO MÚTUO DOS DESPACHANTES DE TRÂNSITO DE SANTA CATARINA E RI",
            "04814563" to "BANCO SOROCRED S.A. - BANCO MÚLTIPLO",
            "04866275" to "Banco Inbursa S.A.",
            "04902979" to "BANCO DA AMAZONIA S.A.",
            "04913129" to "Confidence Corretora de Câmbio S.A.",
            "04913711" to "Banco do Estado do Pará S.A.",
            "05192316" to "Via Certa Financiadora S.A. - Crédito, Financiamento e Investimentos",
            "05351887" to "ZEMA CRÉDITO, FINANCIAMENTO E INVESTIMENTO S/A",
            "05442029" to "Casa do Crédito S.A. Sociedade de Crédito ao Microempreendedor",
            "05463212" to "Cooperativa Central de Crédito - Ailos",
            "05491616" to "COOPERATIVA DE CRÉDITO, POUPANÇA E SERVIÇOS FINANCEIROS DO CENTRO OESTE",
            "05684234" to "PLANNER SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR S.A.",
            "05790149" to "Central Cooperativa de Crédito no Estado do Espírito Santo - CECOOP",
            "05841967" to "COOPERATIVA DE ECONOMIA E CRÉDITO MÚTUO DOS FABRICANTES DE CALÇADOS DE SAPIRANGA",
            "06271464" to "Banco Bradesco BBI S.A.",
            "07207996" to "Banco Bradesco Financiamentos S.A.",
            "07237373" to "Banco do Nordeste do Brasil S.A.",
            "07450604" to "China Construction Bank (Brasil) Banco Múltiplo S/A",
            "07512441" to "HS FINANCEIRA S/A CREDITO, FINANCIAMENTO E INVESTIMENTOS",
            "07652226" to "Lecca Crédito, Financiamento e Investimento S/A",
            "07656500" to "Banco KDB do Brasil S.A.",
            "07679404" to "BANCO TOPÁZIO S.A.",
            "07693858" to "HSCM - SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E À EMPRESA DE PEQUENO PORTE LT",
            "07853842" to "COOPERATIVA DE CRÉDITO RURAL DE OURO   SULCREDI/OURO",
            "07945233" to "PÓLOCRED   SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E À EMPRESA DE PEQUENO PORT",
            "08240446" to "COOPERATIVA DE CREDITO RURAL DE IBIAM - SULCREDI/IBIAM",
            "08253539" to "Cooperativa de Crédito Rural de São Miguel do Oeste - Sulcredi/São Miguel",
            "08357240" to "Banco CSF S.A.",
            "08561701" to "Pagseguro Internet S.A.",
            "08609934" to "MONEYCORP BANCO DE CÂMBIO S.A.",
            "08673569" to "F.D'GOLD - DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA.",
            "09089356" to "GERENCIANET S.A.",
            "09105360" to "ICAP do Brasil Corretora de Títulos e Valores Mobiliários Ltda.",
            "09210106" to "SOCRED S.A. - SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E À EMPRESA DE PEQUENO P",
            "09274232" to "STATE STREET BRASIL S.A. ? BANCO COMERCIAL",
            "09313766" to "CARUANA S.A. - SOCIEDADE DE CRÉDITO, FINANCIAMENTO E INVESTIMENTO",
            "09464032" to "MIDWAY S.A. - CRÉDITO, FINANCIAMENTO E INVESTIMENTO",
            "09512542" to "Codepe Corretora de Valores e Câmbio S.A.",
            "09516419" to "Banco Original do Agronegócio S.A.",
            "09554480" to "Super Pagamentos e Administração de Meios Eletrônicos S.A.",
            "10264663" to "BancoSeguro S.A.",
            "10398952" to "CONFEDERAÇÃO NACIONAL DAS COOPERATIVAS CENTRAIS DE CRÉDITO E ECONOMIA FAMILIAR E",
            "10573521" to "MERCADOPAGO.COM REPRESENTACOES LTDA.",
            "10664513" to "Banco Agibank S.A.",
            "10690848" to "Banco da China Brasil S.A.",
            "10853017" to "Get Money Corretora de Câmbio S.A.",
            "10866788" to "Banco Bandepe S.A.",
            "11165756" to "GLOBAL FINANÇAS SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E À EMPRESA DE PEQUENO",
            "11285104" to "Biorc Financeira - Crédito, Financiamento e Investimento S.A.",
            "11476673" to "BANCO RANDON S.A.",
            "11495073" to "OM DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA",
            "11581339" to "MONEY PLUS SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E A EMPRESA DE PEQUENO PORT",
            "11703662" to "Travelex Banco de Câmbio S.A.",
            "11758741" to "Banco Finaxis S.A.",
            "11970623" to "BANCO SENFF S.A.",
            "12865507" to "BRK S.A. Crédito, Financiamento e Investimento",
            "13009717" to "Banco do Estado de Sergipe S.A.",
            "13059145" to "BEXS BANCO DE CÂMBIO S/A",
            "13140088" to "Acesso Soluções de Pagamento S.A.",
            "13220493" to "BR Partners Banco de Investimento S.A.",
            "13293225" to "Órama Distribuidora de Títulos e Valores Mobiliários S.A.",
            "13370835" to "BPP Instituição de Pagamento S.A.",
            "13486793" to "BRL Trust Distribuidora de Títulos e Valores Mobiliários S.A.",
            "13673855" to "Fram Capital Distribuidora de Títulos e Valores Mobiliários S.A.",
            "13720915" to "Banco Western Union do Brasil S.A.",
            "13884775" to "HUB PAGAMENTOS S.A",
            "14190547" to "CAMBIONET CORRETORA DE CÂMBIO LTDA.",
            "14388334" to "PARANÁ BANCO S.A.",
            "14511781" to "BARI COMPANHIA HIPOTECÁRIA",
            "15111975" to "IUGU SERVICOS NA INTERNET S/A",
            "15114366" to "Banco Bocom BBM S.A.",
            "15173776" to "BANCO CAPITAL S.A.",
            "15357060" to "Banco Woori Bank do Brasil S.A.",
            "15581638" to "Facta Financeira S.A. - Crédito Financiamento e Investimento",
            "16501555" to "Stone Pagamentos S.A.",
            "16927221" to "AMAZÔNIA CORRETORA DE CÂMBIO LTDA.",
            "16944141" to "Broker Brasil Corretora de Câmbio Ltda.",
            "17184037" to "Banco Mercantil do Brasil S.A.",
            "17298092" to "Banco Itaú BBA S.A.",
            "17351180" to "BANCO TRIANGULO S.A.",
            "17352220" to "SENSO CORRETORA DE CAMBIO E VALORES MOBILIARIOS S.A",
            "17453575" to "ICBC do Brasil Banco Múltiplo S.A.",
            "17772370" to "Vip's Corretora de Câmbio Ltda.",
            "17826860" to "BMS SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "18188384" to "CREFAZ SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E A EMPRESA DE PEQUENO PORTE LT",
            "18236120" to "Nu Pagamentos S.A.",
            "18520834" to "UBS Brasil Banco de Investimento S.A.",
            "19307785" to "MS Bank S.A. Banco de Câmbio",
            "19324634" to "LAMARA SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "20155248" to "PARMETAL DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA",
            "21018182" to "BOLETOBANCÁRIO.COM TECNOLOGIA DE PAGAMENTOS LTDA.",
            "21332862" to "CARTOS SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "22610500" to "VORTX DISTRIBUIDORA DE TITULOS E VALORES MOBILIARIOS LTDA.",
            "22896431" to "PICPAY SERVICOS S.A.",
            "23522214" to "Commerzbank Brasil S.A. - Banco Múltiplo",
            "23862762" to "WILL FINANCEIRA S.A. CRÉDITO, FINANCIAMENTO E INVESTIMENTO",
            "24074692" to "GUITTA CORRETORA DE CAMBIO LTDA.",
            "24537861" to "FFA SOCIEDADE DE CRÉDITO AO MICROEMPREENDEDOR E À EMPRESA DE PEQUENO PORTE LTDA.",
            "26563270" to "COOPERATIVA DE CREDITO RURAL DE PRIMAVERA DO LESTE",
            "27098060" to "Banco Digio S.A.",
            "27214112" to "AL5 S.A. CRÉDITO, FINANCIAMENTO E INVESTIMENTO",
            "27302181" to "COOPERATIVA DE CREDITO DOS SERVIDORES DA UNIVERSIDADE FEDERAL DO ESPIRITO SANTO",
            "27351731" to "REALIZE CRÉDITO, FINANCIAMENTO E INVESTIMENTO S.A.",
            "27652684" to "Genial Investimentos Corretora de Valores Mobiliários S.A.",
            "27842177" to "IB Corretora de Câmbio, Títulos e Valores Mobiliários S.A.",
            "28127603" to "BANESTES S.A. BANCO DO ESTADO DO ESPIRITO SANTO",
            "28195667" to "Banco ABC Brasil S.A.",
            "28650236" to "BS2 Distribuidora de Títulos e Valores Mobiliários S.A.",
            "28719664" to "Sistema do Balcão B3",
            "29011780" to "Câmara Interbancária de Pagamentos - CIP C3",
            "29030467" to "Scotiabank Brasil S.A. Banco Múltiplo",
            "29162769" to "TORO CORRETORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA",
            "30306294" to "Banco BTG Pactual S.A.",
            "30680829" to "NU FINANCEIRA S.A. - Sociedade de Crédito, Financiamento e Investimento",
            "30723886" to "Banco Modal S.A.",
            "31597552" to "BANCO CLASSICO S.A.",
            "31749596" to "IDEAL CORRETORA DE TÍTULOS E VALORES MOBILIÁRIOS S.A.",
            "31872495" to "Banco C6 S.A.",
            "31880826" to "Banco Guanabara S.A.",
            "31895683" to "Banco Industrial do Brasil S.A.",
            "32062580" to "Banco Credit Suisse (Brasil) S.A.",
            "32402502" to "QI Sociedade de Crédito Direto S.A.",
            "32648370" to "FAIR CORRETORA DE CAMBIO S.A.",
            "32997490" to "Creditas Sociedade de Crédito Direto S.A.",
            "33042151" to "Banco de la Nacion Argentina",
            "33042953" to "Citibank N.A.",
            "33132044" to "BANCO CEDULA S.A.",
            "33147315" to "Banco Bradesco BERJ S.A.",
            "33172537" to "BANCO J.P. MORGAN S.A.",
            "33264668" to "Banco XP S.A.",
            "33466988" to "Banco Caixa Geral - Brasil S.A.",
            "33479023" to "Banco Citibank S.A.",
            "33603457" to "BANCO RODOBENS S.A.",
            "33644196" to "Banco Fator S.A.",
            "33657248" to "BANCO NACIONAL DE DESENVOLVIMENTO ECONOMICO E SOCIAL",
            "33775974" to "ATIVA INVESTIMENTOS S.A. CORRETORA DE TÍTULOS, CÂMBIO E VALORES",
            "33862244" to "BGC LIQUIDEZ DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA",
            "33885724" to "Banco Itaú Consignado S.A.",
            "33923798" to "Banco Máxima S.A.",
            "34088029" to "LISTO SOCIEDADE DE CREDITO DIRETO S.A.",
            "34111187" to "Haitong Banco de Investimento do Brasil S.A.",
            "34335592" to "ÓTIMO SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "34711571" to "VITREO DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS S.A.",
            "35977097" to "UP.P SOCIEDADE DE EMPRÉSTIMO ENTRE PESSOAS S.A.",
            "36113876" to "OLIVEIRA TRUST DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIARIOS S.A.",
            "36586946" to "BONUSPAGO SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "36947229" to "COBUCCIO SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "37241230" to "SUMUP SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "37526080" to "WORK SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "37715993" to "ACCREDITO - SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "37880206" to "CORA SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "38129006" to "NUMBRS SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "39664698" to "CRED-SYSTEM SOCIEDADE DE CRÉDITO DIRETO S.A.",
            "40303299" to "PORTOPAR DISTRIBUIDORA DE TITULOS E VALORES MOBILIARIOS LTDA.",
            "42272526" to "BNY Mellon Banco S.A.",
            "43180355" to "PEFISA S.A. - CRÉDITO, FINANCIAMENTO E INVESTIMENTO",
            "44189447" to "Banco de La Provincia de Buenos Aires",
            "45246410" to "BANCO GENIAL S.A.",
            "46518205" to "JPMorgan Chase Bank, National Association",
            "48795256" to "Banco AndBank (Brasil) S.A.",
            "49336860" to "ING Bank N.V.",
            "50579044" to "LEVYCAM - CORRETORA DE CAMBIO E VALORES LTDA.",
            "50585090" to "BCV - BANCO DE CRÉDITO E VAREJO S.A.",
            "52904364" to "NECTON INVESTIMENTOS  S.A. CORRETORA DE VALORES MOBILIÁRIOS E COMMODITIES",
            "52937216" to "Bexs Corretora de Câmbio S/A",
            "53518684" to "BANCO HSBC S.A.",
            "54403563" to "Banco Arbi S.A.",
            "54641030" to "Câmara B3",
            "55230916" to "Intesa Sanpaolo Brasil S.A. - Banco Múltiplo",
            "57839805" to "Banco Tricury S.A.",
            "58160789" to "Banco Safra S.A.",
            "58497702" to "Banco Smartbank S.A.",
            "58616418" to "Banco Fibra S.A.",
            "59109165" to "Banco Volkswagen S.A.",
            "59118133" to "Banco Luso Brasileiro S.A.",
            "59274605" to "BANCO GM S.A.",
            "59285411" to "Banco Pan S.A.",
            "59588111" to "Banco Votorantim S.A.",
            "60394079" to "Banco ItauBank S.A.",
            "60498557" to "Banco MUFG Brasil S.A.",
            "60518222" to "Banco Sumitomo Mitsui Brasileiro S.A.",
            "60701190" to "ITAÚ UNIBANCO S.A.",
            "60746948" to "Banco Bradesco S.A.",
            "60814191" to "BANCO MERCEDES-BENZ DO BRASIL S.A.",
            "60850229" to "Omni Banco S.A.",
            "60872504" to "Itaú Unibanco Holding S.A.",
            "60889128" to "BANCO SOFISA S.A.",
            "60934221" to "Câmara de Câmbio B3",
            "61024352" to "BANCO INDUSVAL S.A.",
            "61033106" to "Banco Crefisa S.A.",
            "61088183" to "Banco Mizuho do Brasil S.A.",
            "61182408" to "Banco Investcred Unibanco S.A.",
            "61186680" to "Banco BMG S.A.",
            "61348538" to "BANCO C6 CONSIGNADO S.A.",
            "61444949" to "Sagitur Corretora de Câmbio Ltda.",
            "61533584" to "BANCO SOCIETE GENERALE BRASIL S.A.",
            "61723847" to "Magliano S.A. Corretora de Cambio e Valores Mobiliarios",
            "61747085" to "TULLETT PREBON BRASIL CORRETORA DE VALORES E CÂMBIO LTDA",
            "61809182" to "CREDIT SUISSE HEDGING-GRIFFO CORRETORA DE VALORES S.A",
            "61820817" to "Banco Paulista S.A.",
            "62073200" to "Bank of America Merrill Lynch Banco Múltiplo S.A.",
            "62109566" to "CREDISAN COOPERATIVA DE CRÉDITO",
            "62144175" to "Banco Pine S.A.",
            "62169875" to "Easynvest - Título Corretora de Valores SA",
            "62232889" to "Banco Daycoval S.A.",
            "62237649" to "CAROL DISTRIBUIDORA DE TITULOS E VALORES MOBILIARIOS LTDA.",
            "62285390" to "SINGULARE CORRETORA DE TÍTULOS E VALORES MOBILIÁRIOS S.A.",
            "62287735" to "RENASCENCA DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA",
            "62331228" to "DEUTSCHE BANK S.A. - BANCO ALEMAO",
            "62421979" to "Banco Cifra S.A.",
            "65913436" to "Guide Investimentos S.A. Corretora de Valores",
            "67030395" to "PLANNER TRUSTEE DISTRIBUIDORA DE TÍTULOS E VALORES MOBILIÁRIOS LTDA.",
            "68757681" to "SIMPAUL CORRETORA DE CAMBIO E VALORES MOBILIARIOS  S.A.",
            "68900810" to "Banco Rendimento S.A.",
            "71027866" to "Banco BS2 S.A.",
            "71590442" to "Lastro RDV Distribuidora de Títulos e Valores Mobiliários Ltda.",
            "71677850" to "Frente Corretora de Câmbio Ltda.",
            "73622748" to "B&T CORRETORA DE CAMBIO LTDA.",
            "74828799" to "Novo Banco Continental S.A. - Banco Múltiplo",
            "75647891" to "BANCO CRÉDIT AGRICOLE BRASIL S.A.",
            "76461557" to "Cooperativa de Crédito Rural Coopavel",
            "76543115" to "Banco Sistema S.A.",
            "76641497" to "DOURADA CORRETORA DE CÂMBIO LTDA.",
            "78157146" to "Credialiança Cooperativa de Crédito Rural",
            "78626983" to "Banco VR S.A.",
            "78632767" to "Banco Ourinvest S.A.",
            "80271455" to "BANCO RNX S.A.",
            "81723108" to "CREDICOAMO CREDITO RURAL COOPERATIVA",
            "89960090" to "RB INVESTIMENTOS DISTRIBUIDORA DE TITULOS E VALORES MOBILIARIOS LIMITADA",
            "90400888" to "BANCO SANTANDER (BRASIL) S.A.",
            "91884981" to "Banco John Deere S.A.",
            "92702067" to "Banco do Estado do Rio Grande do Sul S.A.",
            "92856905" to "ADVANCED CORRETORA DE CÂMBIO LTDA",
            "92874270" to "BANCO DIGIMAIS S.A.",
            "92875780" to "WARREN CORRETORA DE VALORES MOBILIÁRIOS E CÂMBIO LTDA.",
            "92894922" to "Banco Original S.A.",
            "94968518" to "DECYSEO CORRETORA DE CAMBIO LTDA."
        )
    }

    fun nameByISPB(ispb: String) =
        banks[ispb] ?: throw IllegalStateException("Instituição não encontrada pelo ISPB: $ispb")

}
