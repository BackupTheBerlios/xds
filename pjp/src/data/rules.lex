15
abcdefghijklmnopqrstuvwxyz0'_:;<>=+-*/.()"e,{}~

# Na prvni radku MUSI byt uveden pocet automatu, na druhem MUSI byt abeceda
# a oznacuje vsechny pismena, 0 vsechny cislice, ' bile znaky
# N: nazev symbolu
# Q: stavy, startovaci musi koncit na I, akceptujici na F
# d: jednotlive prechody, nedefinovane prechody budou vytvoreny automaticky
# Kdyz bude cas a chut, tak to hodim do xml :) Pro zajimavost - vysledny automat tohoto ma 20 stavu :)

# Relacni operatory
N: relation
Q: 1I 2F 3F 4F 5F 6F
d: 1I < 2F
d: 1I > 4F
d: 1I = 6F
d: 2F > 3F
d: 2F = 3F
d: 4F = 5F

# Operator zretezeni
N: concat
Q: 1I 2F
d: 1I . 2F

# Carka
N: comma
Q: 1I 2F
d: 1I , 2F

# Dvojtecka
N: colon
Q: 1I 2F
d: 1I : 2F

# Identifikatory
N: ident
Q: 1I 2F
d: 1I a 2F
d: 1I b 2F
d: 1I c 2F
d: 1I d 2F
d: 1I e 2F
d: 1I f 2F
d: 1I g 2F
d: 1I h 2F
d: 1I i 2F
d: 1I j 2F
d: 1I k 2F
d: 1I l 2F
d: 1I m 2F
d: 1I n 2F
d: 1I o 2F
d: 1I p 2F
d: 1I q 2F
d: 1I r 2F
d: 1I s 2F
d: 1I t 2F
d: 1I u 2F
d: 1I v 2F
d: 1I w 2F
d: 1I x 2F
d: 1I y 2F
d: 1I z 2F
d: 2F a 2F
d: 2F b 2F
d: 2F c 2F
d: 2F d 2F
d: 2F e 2F
d: 2F f 2F
d: 2F g 2F
d: 2F h 2F
d: 2F i 2F
d: 2F j 2F
d: 2F k 2F
d: 2F l 2F
d: 2F m 2F
d: 2F n 2F
d: 2F o 2F
d: 2F p 2F
d: 2F q 2F
d: 2F r 2F
d: 2F s 2F
d: 2F t 2F
d: 2F u 2F
d: 2F v 2F
d: 2F w 2F
d: 2F x 2F
d: 2F y 2F
d: 2F z 2F
d: 2F 0 2F
d: 2F _ 2F

# Leva zavorka
N: leftp
Q: 1I 2F
d: 1I ( 2F

# Prava zavorka
N: rightp
Q: 1I 2F
d: 1I ) 2F

# Strednik
N: semicolon
Q: 1I 2F
d: 1I ; 2F

# Assign :=
N: assign
Q: 1I 2 3F
d: 1I : 2
d: 2 = 3F

# Prirozene cislo
N: numberint
Q: 1I 2F
d: 1I 0 2F
d: 2F 0 2F

# Cele cislo
N: numberdouble
Q: 1I 2 3 6F 4 5F 7
d: 1I 0 2
d: 2 0 2
d: 2 . 3
d: 3 0 6F
d: 2 e 4
d: 6F e 4
d: 4 - 7
d: 7 0 5F
d: 4 0 5F
d: 6F 0 6F
d: 5F 0 5F

# Retezec
N: stringval
Q: 1I 2 3F
d: 1I " 2
d: 2 " 3F
d: 2 ~ 2

# Komentar
N: komentar
Q: 1I 2 3F
d: 1I / 2
d: 2 / 3F
d: 3F ~ 3F

# Aritmeticke operatory
N: arithmeticala
Q: 1I 2F
d: 1I * 2F
d: 1I / 2F

N: arithmeticalb
Q: 1I 2F
d: 1I + 2F
d: 1I - 2F