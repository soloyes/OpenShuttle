attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
void main(){
    v_color=a_color;
    // При передаче цвет из SpriteBatch в шейдер, происходит преобразование из ABGR int цвета в float.
    // что-бы избежать NAN при преобразовании, доступен не весь диапазон для альфы, а только значения от (0-254)
    //чтобы полностью передать непрозрачность цвета, когда альфа во float равна 1, то всю альфу приходится умножать.
    //это специфика libgdx и о ней надо помнить при переопределении вершинного шейдера.
    v_color.a = v_color.a * (255.0/254.0);
    v_texCoords = a_texCoord0;
    gl_Position =  u_projTrans * a_position;
}