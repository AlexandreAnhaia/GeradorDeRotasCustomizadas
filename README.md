# GeradorDeRotasCustomizadas

Para gerar uma nova rota, é necessário editar o arquivo de configuração chamando "configuracao.json".

O arquivo é no formato JSON (JavaScript Object Notation), que significa um formato compacto para troca de dados/informações simples e rápida de modo organizado e claro, onde os dados são representados por chave/valor respectivamente, por exemplo: "{ "nome": "Alexandre", "idade": 26 }", pode ser observado no exemplo a representação de um objeto Pessoa, que possui dois atributos nome e idade.

Portanto, o arquivo de configuração é composto por onze atributos, sendo eles: arquivo, pontosPreMissao, direcao, movimento, droneString, cameraString, blurFactor, tamanhoCartaoSD, distanciaFotos, zoom e sobreposicao. Vou explicar detalhadamente cada um deles a seguir.

arquivo - É o nome do arquivo inicial passado como parâmetro para obter os pontos da pré-missão, missão e da pós-missão;
pontosPreMissao - É a quantidade de pontos que a rota da pré-missão vai possuir;
direcao - É a direção que o drone vai percorrer durante a missão;
movimento - É o movimento que o drone vai executar na missão;
droneString - É o nome/modelo do drone que vai executar a missão;
cameraString - É o nome/modelo da câmera que vai estar no drone para a execução da missão;
blurFacotr - É uma informação referente a câmera que vai ser usada na missão;
tamanhoCartaoSD - É o tamanho do cartão de memória que está no drone;
distanciaFotos - É a distância em metros do drone em relação a estrutura a ser observada;
zoom - É o zoom que o drone vai estar durante a missão;
sobreposicao - É a quantidade de sobreposição de uma foto a próxima;
